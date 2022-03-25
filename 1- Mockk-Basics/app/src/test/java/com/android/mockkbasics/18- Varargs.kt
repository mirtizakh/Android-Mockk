package com.android.mockkbasics

import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class Varargs {

    interface ClsWithManyMany {
        fun manyMany(vararg x: Any): Int
    }
    @Test
    fun varargs() {

        val obj = mockk<ClsWithManyMany>()

        every { obj.manyMany(5, 6, *varargAll { it == 7 }) } returns 3

        println(obj.manyMany(5, 6, 7)) // 3
        println(obj.manyMany(5, 6, 7, 7)) // 3
        println(obj.manyMany(5, 6, 7, 7, 7)) // 3

        every { obj.manyMany(5, 6, *anyVararg(), 7) } returns 4

        println(obj.manyMany(5, 6, 1, 7)) // 4
        println(obj.manyMany(5, 6, 2, 3, 7)) // 4
        println(obj.manyMany(5, 6, 4, 5, 6, 7)) // 4

        every { obj.manyMany(5, 6, *varargAny { nArgs > 5 }, 7) } returns 5

        println(obj.manyMany(5, 6, 4, 5, 6, 7)) // 5
        println(obj.manyMany(5, 6, 4, 5, 6, 7, 7)) // 5

        every {
            obj.manyMany(5, 6, *varargAny {
                if (position < 3) it == 3 else it == 4
            }, 7)
        } returns 6

        println(obj.manyMany(5, 6, 3, 4, 7)) // 6
        println(obj.manyMany(5, 6, 3, 4, 4, 7)) // 6
    }
}