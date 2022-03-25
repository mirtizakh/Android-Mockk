package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ExtensionFunctions {
    /*
    There are three cases of extension function:

    class wide
    object wide
    module wide
    In case of an object or a class, you can mock extension functions just by creating a regular mockk:
     */

    data class Obj(val value: Int)

    class Ext {
        fun Obj.extensionFunc() = value + 5
    }

    @Test
    fun extensionFunctions() {
        with(mockk<Ext>()) {
            every {
                Obj(5).extensionFunc()
            } returns 11

            assertEquals(11, Obj(5).extensionFunc())

            verify {
                Obj(5).extensionFunc()
            }
        }
    }

    /*
    To mock module wide extension functions you need to build mockkStatic(...)
    with as argument the module's class name. For example "pkg.FileKt" for module File.kt in the pkg package.
     */

    data class ObjModule(val value: Int)

    // declared in File.kt ("pkg" package)
    fun ObjModule.extensionFunc() = value + 5

    // We need to put the function in File.kt for testing
    @Test
    fun moduleExtensionFunctions() {
        mockkStatic("pkg.FileKt")

        every {
            ObjModule(5).extensionFunc()
        } returns 11

        assertEquals(11, ObjModule(5).extensionFunc())

        verify {
            ObjModule(5).extensionFunc()
        }
    }

}