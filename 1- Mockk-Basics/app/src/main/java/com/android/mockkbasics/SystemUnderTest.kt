package com.android.mockkbasics

class Dependency1(val value1: Int) {
    fun call(value: Int): Int {
        return value
    }

    fun result(value: Int) {
        // doing some work here
    }

}

class Dependency2(val value2: String) {
    fun call(value: Int): Int {

        return  value
    }

    fun result(value: Int) {
        // doing some work here
    }

}

class SystemUnderTest(
    private val dependency1: Dependency1,
    private val dependency2: Dependency2
) {
    fun calculate() =
        dependency1.value1 + dependency2.value2.toInt()

    fun calculateWithCall(valueOfDoc1: Int, valueOfDoc2: Int) =
        dependency1.call(valueOfDoc1) + dependency2.call(valueOfDoc2)
}