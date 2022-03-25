# Android-Mockk
## Table of contents
* [1- Some useful resources related to Mockk](#1-Some-useful-resources-related-to-Mockk)
* [2- MockK](#2-MockK)

### 1-Some useful resources related to Mockk
* Documentation. https://mockk.io/
* Basic features of the MockK library. https://androidrepo.com/repo/mockk-mockk-android-test <br> https://www.baeldung.com/kotlin/mockk
* Mockk with MVP https://marco-cattaneo.medium.com/kotlin-unit-testing-with-mockk-91d52aea2852
* Argument matchers , expected behaviour and behaviour verification <br> https://blog.kotlin-academy.com/mocking-is-not-rocket-science-expected-behavior-and-behavior-verification-3862dd0e0f03
* Captured arguments, relaxed mocks, spies and annotations. <br> https://blog.kotlin-academy.com/mocking-is-not-rocket-science-mockk-features-e5d55d735a98
* Test Kotlin coroutines with MockK <br> https://marco-cattaneo.medium.com/how-use-and-test-kotlin-coroutines-with-mockk-library-49ddb2c9ee5f
<br> https://blog.logrocket.com/better-kotlin-coroutine-unit-testing/
 
### 2-MockK
* In Kotlin, all classes and methods are final. While this helps us write immutable code, it also causes some problems during testing.

* Most JVM mock libraries have problems with mocking or stubbing final classes. Of course, we can add the “open” keyword to classes and methods that we want to mock. But changing classes only for mocking some code doesn’t feel like the best approach.

* Here comes the MockK library, which offers support for Kotlin language features and constructs. MockK main intention is to be a convenient mocking library for everybody who develops in Kotlin.

Why is it better than a well known Mockito library for Kotlin?<br>
Mockk supports some important language features within Kotlin.

#### 1- Final by default(Classes and methods in kotlin) :
  Concerning Java, Kotlin classes (and methods) are final by default. That means Mockito requires some extra things to make it to work, whereas Mockk can    do this efficiently without any extra things.
####  2- Object mocking : 
Kotlin objects mean Java statics. Mockito alone doesn’t support mocking of statics. There are the same other frameworks required with Mockito to write tests, but again Mockk provides this without any extra things. <br>
Example : <br>
 mockObject(MyObject) <br>
 every { MyObject.someMethod() } returns "Something"
 #### 3- Extension functions :
 Since extension functions map to Java statics, again, Mockito doesn’t support mocking them. With Mockk, you can mock them without any extra configuration.
  #### 4- Chained mocking :
  With Mockk you can chain your mocking, with this we can mock statements quite easily like you can see in the example below. We are using every{} block to mock methods.<br>
  Example : <br>
  val mockedClass = mockk()<br>
 every { mockedClass.someMethod().someOtherMethod() } returns "Something"
  #### 5- Mocking static methods
   Mocking static methods is not easy in mockito but using mockK java static methods can easily be mocked using  mockStatic.<br>
   Example : <br>
   mockkStatic(TextUtils::class)<br>
   @Test<br>
   fun validateString() {<br>
   every { TextUtils.isEmpty(param } returns true<br>
   }<br> <br>
   
   ### Examples 
  
   Simplest example. By default mocks are strict, so you need to provide some behaviour.

```kotlin
val car = mockk<Car>()

every { car.drive(Direction.NORTH) } returns Outcome.OK

car.drive(Direction.NORTH) // returns OK

verify { car.drive(Direction.NORTH) }

confirmVerified(car)
```

### Annotations

You can use annotations to simplify the creation of mock objects:

```kotlin

class TrafficSystem {
  lateinit var car1: Car
  
  lateinit var car2: Car
  
  lateinit var car3: Car
}

class CarTest {
  @MockK
  lateinit var car1: Car

  @RelaxedMockK
  lateinit var car2: Car

  @MockK(relaxUnitFun = true)
  lateinit var car3: Car

  @SpyK
  var car4 = Car()
  
  @InjectMockKs
  var trafficSystem = TrafficSystem()
  
  @Before
  fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true) // turn relaxUnitFun on for all mocks

  @Test
  fun calculateAddsValues1() {
      // ... use car1, car2, car3 and car4
  }
}
```

Injection first tries to match properties by name, then by class or superclass. 
Check the `lookupType` parameter for customization. 

Properties are injected even if `private` is applied. Constructors for injection are selected from the biggest 
number of arguments to lowest.

`@InjectMockKs` by default injects only `lateinit var`s or `var`s that are not assigned. 
To change this, use `overrideValues = true`. This would assign the value even if it is already initialized somehow.
To inject `val`s, use `injectImmutable = true`. For a shorter notation use `@OverrideMockKs` which does the same as 
`@InjectMockKs` by default, but turns these two flags on.

#### JUnit5

In JUnit5 you can use `MockKExtension` to initialize your mocks. 

```kotlin
@ExtendWith(MockKExtension::class)
class CarTest {
  @MockK
  lateinit var car1: Car

  @RelaxedMockK
  lateinit var car2: Car

  @MockK(relaxUnitFun = true)
  lateinit var car3: Car

  @SpyK
  var car4 = Car()

  @Test
  fun calculateAddsValues1() {
      // ... use car1, car2, car3 and car4
  }
}
```

Additionally, it adds the possibility to use `@MockK` and `@RelaxedMockK` on test function parameters:

```kotlin
@Test
fun calculateAddsValues1(@MockK car1: Car, @RelaxedMockK car2: Car) {
  // ... use car1 and car2
}
```

Finally, this extension will call `unmockkAll` in a `@AfterAll` callback, ensuring your test environment is clean after
each test class execution.
You can disable this behavior by adding the `@MockKExtension.KeepMocks` annotation to your class or globally by setting 
the `mockk.junit.extension.keepmocks=true` property

### Spy

Spies allow you to mix mocks and real objects.

```kotlin
val car = spyk(Car()) // or spyk<Car>() to call the default constructor

car.drive(Direction.NORTH) // returns whatever the real function of Car returns

verify { car.drive(Direction.NORTH) }

confirmVerified(car)
```

Note: the spy object is a copy of the passed object.

### Relaxed mock

A `relaxed mock` is the mock that returns some simple value for all functions. 
This allows you to skip specifying behavior for each case, while still stubbing things you need.
For reference types, chained mocks are returned.

```kotlin
val car = mockk<Car>(relaxed = true)

car.drive(Direction.NORTH) // returns null

verify { car.drive(Direction.NORTH) }

confirmVerified(car)
```

Note: relaxed mocking is working badly with generic return types. A class cast exception is usually thrown in this case.
Opt for stubbing manually in the case of a generic return type.

Workaround:

```kotlin
val func = mockk<() -> Car>(relaxed = true) // in this case invoke function has generic return type

// this line is workaround, without it the relaxed mock would throw a class cast exception on the next line
every { func() } returns Car() // or you can return mockk() for example 

func()
```

### Partial mocking

Sometimes, you need to stub some functions, but still call the real method on others, or on specific arguments.
This is possible by passing `callOriginal()` to `answers`, which works for both relaxed and non-relaxed mocks.

```kotlin
class Adder {
 fun addOne(num: Int) = num + 1
}

val adder = mockk<Adder>()

every { adder.addOne(any()) } returns -1
every { adder.addOne(3) } answers { callOriginal() }

assertEquals(-1, adder.addOne(2))
assertEquals(4, adder.addOne(3)) // original function is called
```

### Mock relaxed for functions returning Unit

If you want `Unit`-returning functions to be relaxed, you can use `relaxUnitFun = true` as an argument to the `mockk` function, 
`@MockK`annotation or `MockKAnnotations.init` function.

Function:
```kotlin
mockk<ClassBeingMocked>(relaxUnitFun = true)
```

Annotation:
```kotlin
@MockK(relaxUnitFun = true)
lateinit var mock1: ClassBeingMocked
init {
    MockKAnnotations.init(this)
}
```

MockKAnnotations.init:
```kotlin
@MockK
lateinit var mock2: ClassBeingMocked
init {
    MockKAnnotations.init(this, relaxUnitFun = true)
}
```

### Object mocks

Objects can be turned into mocks in the following way:

```kotlin
object ObjBeingMocked {
  fun add(a: Int, b: Int) = a + b
}

mockkObject(ObjBeingMocked) // applies mocking to an Object

assertEquals(3, ObjBeingMocked.add(1, 2))

every { ObjBeingMocked.add(1, 2) } returns 55

assertEquals(55, ObjBeingMocked.add(1, 2))
```

To revert back, use `unmockkAll` or `unmockkObject`:

```kotlin
@Before
fun beforeTests() {
    mockkObject(ObjBeingMocked)
    every { MockObj.add(1,2) } returns 55
}

@Test
fun willUseMockBehaviour() {
    assertEquals(55, ObjBeingMocked.add(1,2))
}

@After
fun afterTests() {
    unmockkAll()
    // or unmockkObject(ObjBeingMocked)
}
```

Despite the Kotlin language restrictions, you can create new instances of objects if required by testing logic:
```kotlin
val newObjectMock = mockk<MockObj>()
```

### Class mock

Sometimes you need a mock of an arbitrary class. Use `mockkClass` in those cases.

```kotlin
val car = mockkClass(Car::class)

every { car.drive(Direction.NORTH) } returns Outcome.OK

car.drive(Direction.NORTH) // returns OK

verify { car.drive(Direction.NORTH) }
```

### Enumeration mocks

Enums can be mocked using `mockkObject`:

```kotlin
enum class Enumeration(val goodInt: Int) {
    CONSTANT(35),
    OTHER_CONSTANT(45);
}

mockkObject(Enumeration.CONSTANT)
every { Enumeration.CONSTANT.goodInt } returns 42
assertEquals(42, Enumeration.CONSTANT.goodInt)
```

### Constructor mocks

Sometimes, especially in code you don't own, you need to mock newly created objects.
For this purpose, the following constructs are provided:

```kotlin
class MockCls {
  fun add(a: Int, b: Int) = a + b
}

mockkConstructor(MockCls::class)

every { anyConstructed<MockCls>().add(1, 2) } returns 4

assertEquals(4, MockCls().add(1, 2)) // note new object is created

verify { anyConstructed<MockCls>().add(1, 2) }
```

The basic idea is that just after the constructor of the mocked class is executed (any of them), objects become a `constructed mock`.  
Mocking behavior of such a mock is connected to the special `prototype mock` denoted by `anyConstructed<MockCls>()`.  
There is one instance per class of such a `prototype mock`. Call recording also happens to the `prototype mock`.  
If no behavior for the function is specified, then the original function is executed.  

In case a class has more than one constructor, each can be mocked separately:

```kotlin
class MockCls(private val a: Int = 0) {
  constructor(x: String) : this(x.toInt())  
  fun add(b: Int) = a + b
}

mockkConstructor(MockCls::class)

every { constructedWith<MockCls>().add(1) } returns 2
every { 
    constructedWith<MockCls>(OfTypeMatcher<String>(String::class)).add(2) // Mocks the constructor which takes a String
} returns 3
every {
    constructedWith<MockCls>(EqMatcher(4)).add(any()) // Mocks the constructor which takes an Int
} returns 4

assertEquals(2, MockCls().add(1))
assertEquals(3, MockCls("2").add(2))
assertEquals(4, MockCls(4).add(7))

verify { 
    constructedWith<MockCls>().add(1)
    constructedWith<MockCls>("2").add(2)
    constructedWith<MockCls>(EqMatcher(4)).add(7)
}
```

Note that in this case, a `prototype mock` is created for every set of argument matchers passed to `constructedWith`.


### Partial argument matching

You can mix both regular arguments and matchers:

```kotlin
val car = mockk<Car>()

every { 
  car.recordTelemetry(
    speed = more(50),
    direction = Direction.NORTH, // here eq() is used
    lat = any(),
    long = any()
  )
} returns Outcome.RECORDED

car.recordTelemetry(60, Direction.NORTH, 51.1377382, 17.0257142)

verify { car.recordTelemetry(60, Direction.NORTH, 51.1377382, 17.0257142) }

confirmVerified(car)
```

### Chained calls

You can stub chains of calls:

```kotlin
val car = mockk<Car>()

every { car.door(DoorType.FRONT_LEFT).windowState() } returns WindowState.UP

car.door(DoorType.FRONT_LEFT) // returns chained mock for Door
car.door(DoorType.FRONT_LEFT).windowState() // returns WindowState.UP

verify { car.door(DoorType.FRONT_LEFT).windowState() }

confirmVerified(car)
```

Note: if the function's return type is generic then the information about the actual type is gone.  
To make chained calls work, additional information is required.  
Most of the time the framework will catch the cast exception and do `autohinting`.  
In the case it is explicitly required, use `hint` before making the next call.  

```kotlin
every { obj.op2(1, 2).hint(Int::class).op1(3, 4) } returns 5
```

### Hierarchical mocking

From version 1.9.1 mocks may be chained into hierarchies:

```kotlin
interface AddressBook {
    val contacts: List<Contact>
}

interface Contact {
    val name: String
    val telephone: String
    val address: Address
}

interface Address {
    val city: String
    val zip: String
}

val addressBook = mockk<AddressBook> {
    every { contacts } returns listOf(
        mockk {
            every { name } returns "John"
            every { telephone } returns "123-456-789"
            every { address.city } returns "New-York"
            every { address.zip } returns "123-45"
        },
        mockk {
            every { name } returns "Alex"
            every { telephone } returns "789-456-123"
            every { address } returns mockk {
                every { city } returns "Wroclaw"
                every { zip } returns "543-21"
            }
        }
    )
}
```

### Capturing

You can capture an argument to a `CapturingSlot` or `MutableList`:

```kotlin
val car = mockk<Car>()

val slot = slot<Double>()
val list = mutableListOf<Double>()

every {
  car.recordTelemetry(
    speed = capture(slot), // makes mock match calls with any value for `speed` and record it in a slot
    direction = Direction.NORTH // makes mock and capturing only match calls with specific `direction`. Use `any()` to match calls with any `direction`
  )
} answers {
  println(slot.captured)

  Outcome.RECORDED
}


every {
  car.recordTelemetry(
    speed = capture(list),
    direction = Direction.SOUTH
  )
} answers {
  println(list)

  Outcome.RECORDED
}

car.recordTelemetry(speed = 15, direction = Direction.NORTH) // prints 15
car.recordTelemetry(speed = 16, direction = Direction.SOUTH) // prints 16

verify(exactly = 2) { car.recordTelemetry(speed = or(15, 16), direction = any()) }

confirmVerified(car)
```

### Verification atLeast, atMost or exactly times

You can check the call count with the `atLeast`, `atMost` or `exactly` parameters:

```kotlin

val car = mockk<Car>(relaxed = true)

car.accelerate(fromSpeed = 10, toSpeed = 20)
car.accelerate(fromSpeed = 10, toSpeed = 30)
car.accelerate(fromSpeed = 20, toSpeed = 30)

// all pass
verify(atLeast = 3) { car.accelerate(allAny()) }
verify(atMost  = 2) { car.accelerate(fromSpeed = 10, toSpeed = or(20, 30)) }
verify(exactly = 1) { car.accelerate(fromSpeed = 10, toSpeed = 20) }
verify(exactly = 0) { car.accelerate(fromSpeed = 30, toSpeed = 10) } // means no calls were performed

confirmVerified(car)
```

### Verification order

* `verifyAll` verifies that all calls happened without checking their order.
* `verifySequence` verifies that the calls happened in a specified sequence.
* `verifyOrder` verifies that calls happened in a specific order.
* `wasNot Called` verifies that the mock (or the list of mocks) was not called at all.

```kotlin
class MockedClass {
    fun sum(a: Int, b: Int) = a + b
}

val obj = mockk<MockedClass>()
val slot = slot<Int>()

every {
    obj.sum(any(), capture(slot))
} answers {
    1 + firstArg<Int>() + slot.captured
}

obj.sum(1, 2) // returns 4
obj.sum(1, 3) // returns 5
obj.sum(2, 2) // returns 5

verifyAll {
    obj.sum(1, 3)
    obj.sum(1, 2)
    obj.sum(2, 2)
}

verifySequence {
    obj.sum(1, 2)
    obj.sum(1, 3)
    obj.sum(2, 2)
}

verifyOrder {
    obj.sum(1, 2)
    obj.sum(2, 2)
}

val obj2 = mockk<MockedClass>()
val obj3 = mockk<MockedClass>()
verify {
    listOf(obj2, obj3) wasNot Called
}

confirmVerified(obj)
```

### Verification confirmation

To double check that all calls were verified by `verify...` constructs, you can use `confirmVerified`:

```
confirmVerified(mock1, mock2)
```

It doesn't make much sense to use it for `verifySequence` and `verifyAll`, as these verification methods already exhaustively cover all calls with verification.

It will throw an exception if there are some calls left without verification.

Some calls can be excluded from this confirmation, check the next section for more details.

```
val car = mockk<Car>()

every { car.drive(Direction.NORTH) } returns Outcome.OK
every { car.drive(Direction.SOUTH) } returns Outcome.OK

car.drive(Direction.NORTH) // returns OK
car.drive(Direction.SOUTH) // returns OK

verify {
    car.drive(Direction.SOUTH)
    car.drive(Direction.NORTH)
}

confirmVerified(car) // makes sure all calls were covered with verification
```

### Recording exclusions

To exclude unimportant calls from being recorded, you can use `excludeRecords`:

```
excludeRecords { mock.operation(any(), 5) }
```

All matching calls will be excluded from recording. This may be useful if you are using exhaustive verification: `verifyAll`, `verifySequence` or `confirmVerified`.

```
val car = mockk<Car>()

every { car.drive(Direction.NORTH) } returns Outcome.OK
every { car.drive(Direction.SOUTH) } returns Outcome.OK

excludeRecords { car.drive(Direction.SOUTH) }

car.drive(Direction.NORTH) // returns OK
car.drive(Direction.SOUTH) // returns OK

verify {
    car.drive(Direction.NORTH)
}

confirmVerified(car) // car.drive(Direction.SOUTH) was excluded, so confirmation is fine with only car.drive(Direction.NORTH)
```

### Verification timeout

To verify concurrent operations, you can use `timeout = xxx`:

```kotlin
mockk<MockCls> {
    every { sum(1, 2) } returns 4

    Thread {
        Thread.sleep(2000)
        sum(1, 2)
    }.start()

    verify(timeout = 3000) { sum(1, 2) }
}
```

This will wait until one of two following states: either verification is passed or the timeout is reached.

### Returning Unit

If a function returns `Unit`, you can use the `justRun` construct:

```kotlin
class MockedClass {
    fun sum(a: Int, b: Int): Unit {
        println(a + b)
    }
}

val obj = mockk<MockedClass>()

justRun { obj.sum(any(), 3) }

obj.sum(1, 1)
obj.sum(1, 2)
obj.sum(1, 3)

verify {
    obj.sum(1, 1)
    obj.sum(1, 2)
    obj.sum(1, 3)
}
```

Other ways to write `justRun { obj.sum(any(), 3) }`:
 - `every { obj.sum(any(), 3) } just Runs`
 - `every { obj.sum(any(), 3) } returns Unit`
 - `every { obj.sum(any(), 3) } answers { Unit }`

### Coroutines

To mock coroutines you need to add another dependency to the support library.
<table>
<tr>
    <th>Gradle</th>
</tr>
<tr>
    <td>
<pre>testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:x.x"</pre>
    </td>
</tr>
</table>
<table>
<tr>
    <th>Maven</th>
</tr>
<tr>
<td>
    <pre>
&lt;dependency&gt;
    &lt;groupId&gt;org.jetbrains.kotlinx&lt;/groupId&gt;
    &lt;artifactId&gt;kotlinx-coroutines-core&lt;/artifactId&gt;
    &lt;version&gt;x.x&lt;/version&gt;
    &lt;scope&gt;test&lt;/scope&gt;
&lt;/dependency&gt;</pre>
    </td>
</tr>
</table>

Then you can use `coEvery`, `coVerify`, `coMatch`, `coAssert`, `coRun`, `coAnswers` or `coInvoke` to mock suspend functions.

```kotlin
val car = mockk<Car>()

coEvery { car.drive(Direction.NORTH) } returns Outcome.OK

car.drive(Direction.NORTH) // returns OK

coVerify { car.drive(Direction.NORTH) }
```
### Extension functions

There are three types of extension function in Kotlin:

* class-wide
* object-wide
* module-wide

For an object or a class, you can mock extension functions just by creating a regular `mockk`:

```kotlin
data class Obj(val value: Int)

class Ext {
    fun Obj.extensionFunc() = value + 5
}

with(mockk<Ext>()) {
    every {
        Obj(5).extensionFunc()
    } returns 11

    assertEquals(11, Obj(5).extensionFunc())

    verify {
        Obj(5).extensionFunc()
    }
}
```

To mock module-wide extension functions you need to
build `mockkStatic(...)` with the module's class name as an argument.
For example "pkg.FileKt" for module `File.kt` in the `pkg` package.

```kotlin
data class Obj(val value: Int)

// declared in File.kt ("pkg" package)
fun Obj.extensionFunc() = value + 5

mockkStatic("pkg.FileKt")

every {
    Obj(5).extensionFunc()
} returns 11

assertEquals(11, Obj(5).extensionFunc())

verify {
    Obj(5).extensionFunc()
}
```

In `jvm` environments you can replace the class name with a function reference:
```kotlin
mockkStatic(Obj::extensionFunc)
```
Note that this will mock the whole `pkg.FileKt` class, and not just `extensionFunc`. 

This syntax also applies for extension properties:
```kotlin
val Obj.squareValue get() = value * value

mockkStatic(Obj::squareValue)
```

If `@JvmName` is used, specify it as a class name.

KHttp.kt:
```
@file:JvmName("KHttp")

package khttp
// ... KHttp code 
```

Testing code:
```
mockkStatic("khttp.KHttp")
```

Sometimes you need to know a little bit more to mock an extension function. 
For example the extension function `File.endsWith()` has a totally unpredictable `classname`:
```kotlin
   mockkStatic("kotlin.io.FilesKt__UtilsKt")
   every { File("abc").endsWith(any<String>()) } returns true
   println(File("abc").endsWith("abc"))
```
This is standard Kotlin behaviour that may be unpredictable.
Use `Tools -> Kotlin -> Show Kotlin Bytecode` or check `.class` files in JAR archive to detect such names.

### Varargs

From version 1.9.1, more extended vararg handling is possible:

```kotlin
    interface ClsWithManyMany {
        fun manyMany(vararg x: Any): Int
    }

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
```

### Private functions mocking / dynamic calls

IF you need to mock private functions, you can do it via a dynamic call.
```kotlin
class Car {
    fun drive() = accelerate()

    private fun accelerate() = "going faster"
}

val mock = spyk<Car>(recordPrivateCalls = true)

every { mock["accelerate"]() } returns "going not so fast"

assertEquals("going not so fast", mock.drive())

verifySequence {
    mock.drive()
    mock["accelerate"]()
}
```

If you want to verify private calls, you should create a `spyk` with `recordPrivateCalls = true`

Additionally, a more verbose syntax allows you to get and set properties, combined with the same dynamic calls:

```kotlin
val mock = spyk(Team(), recordPrivateCalls = true)

every { mock getProperty "speed" } returns 33
every { mock setProperty "acceleration" value less(5) } just runs
justRun { mock invokeNoArgs "privateMethod" }
every { mock invoke "openDoor" withArguments listOf("left", "rear") } returns "OK"

verify { mock getProperty "speed" }
verify { mock setProperty "acceleration" value less(5) }
verify { mock invoke "openDoor" withArguments listOf("left", "rear") }
```

### Property backing fields

You can access the backing fields via `fieldValue` and use `value` for the value being set.

Note: in the examples below, we use `propertyType` to specify the type of the `fieldValue`.
This is needed because it is possible to capture the type automatically for the getter.
Use `nullablePropertyType` to specify a nullable type.

```kotlin
val mock = spyk(MockCls(), recordPrivateCalls = true)

every { mock.property } answers { fieldValue + 6 }
every { mock.property = any() } propertyType Int::class answers { fieldValue += value }
every { mock getProperty "property" } propertyType Int::class answers { fieldValue + 6 }
every { mock setProperty "property" value any<Int>() } propertyType Int::class answers  { fieldValue += value }
every {
    mock.property = any()
} propertyType Int::class answers {
    fieldValue = value + 1
} andThen {
    fieldValue = value - 1
}
```

### Multiple interfaces

Adding additional behaviours via interfaces and stubbing them:

```kotlin
val spy = spyk(System.out, moreInterfaces = *arrayOf(Runnable::class))

spy.println(555)

every {
    (spy as Runnable).run()
} answers {
    (self as PrintStream).println("Run! Run! Run!")
}

val thread = Thread(spy as Runnable)
thread.start()
thread.join()
```

### Mocking Nothing

Nothing special here. If you have a function returning `Nothing`:

```kotlin
fun quit(status: Int): Nothing {
    exitProcess(status)
}
```

Then you can for example throw an exception as behaviour:

```kotlin
every { quit(1) } throws Exception("this is a test")
```

### Clearing vs Unmocking

* clear - deletes the internal state of objects associated with a mock, resulting in an empty object
* unmock - re-assigns transformation of classes back to original state prior to mock

## Matcher extensibility

A very simple way to create new matchers is by attaching a function 
to `MockKMatcherScope` or `MockKVerificationScope` and using the `match` function:

```
    fun MockKMatcherScope.seqEq(seq: Sequence<String>) = match<Sequence<String>> {
        it.toList() == seq.toList()
    }
```

It's also possible to create more advanced matchers by implementing the `Matcher` interface. 

### Custom matchers

Example of a custom matcher that compares list without order:

```kotlin 

@Test
fun test() {
    class MockCls {
        fun op(a: List<Int>) = a.reversed()
    }

    val mock = mockk<MockCls>()

    every { mock.op(any()) } returns listOf(5, 6, 9)

    println(mock.op(listOf(1, 2, 3)))

    verify { mock.op(matchListWithoutOrder(3, 2, 1)) }

}

data class ListWithoutOrderMatcher<T>(
    val expectedList: List<T>,
    val refEq: Boolean
) : Matcher<List<T>> {
    val map = buildCountsMap(expectedList, refEq)

    override fun match(arg: List<T>?): Boolean {
        if (arg == null) return false
        return buildCountsMap(arg, refEq) == map
    }

    private fun buildCountsMap(list: List<T>, ref: Boolean): Map<Any?, Int> {
        val map = mutableMapOf<Any?, Int>()

        for (item in list) {
            val key = when {
                item == null -> nullKey
                refEq -> InternalPlatform.ref(item)
                else -> item
            }
            map.compute(key, { _, value -> (value ?: 0) + 1 })
        }

        return map
    }

    override fun toString() = "matchListWithoutOrder($expectedList)"

    @Suppress("UNCHECKED_CAST")
    override fun substitute(map: Map<Any, Any>): Matcher<List<T>> {
        return copy(expectedList = expectedList.map { map.getOrDefault(it as Any?, it) } as List<T>)
    }

    companion object {
        val nullKey = Any()
    }
}

inline fun <reified T : List<E>, E : Any> MockKMatcherScope.matchListWithoutOrder(
    vararg items: E,
    refEq: Boolean = true
): T = match(ListWithoutOrderMatcher(listOf(*items), refEq))

```

## Settings file

To adjust parameters globally, there are a few settings you can specify in a resource file.

How to use: 
 1. Create a `io/mockk/settings.properties` file in `src/main/resources`.
 2. Put any of the following options:
```properties
relaxed=true|false
relaxUnitFun=true|false
recordPrivateCalls=true|false
stackTracesOnVerify=true|false
stackTracesAlignment=left|center
```

`stackTracesAlignment` determines whether to align the stack traces to the center (default),
 or to the left (more consistent with usual JVM stackTraces).

## DSL tables

Here are a few tables to help you master the DSL.

### Top level functions

|Function|Description|
|--------|-----------|
|`mockk<T>(...)`|builds a regular mock|
|`spyk<T>()`|builds a spy using the default constructor|
|`spyk(obj)`|builds a spy by copying from `obj`|
|`slot`|creates a capturing slot|
|`every`|starts a stubbing block|
|`coEvery`|starts a stubbing block for coroutines|
|`verify`|starts a verification block|
|`coVerify`|starts a verification block for coroutines|
|`verifyAll`|starts a verification block that should include all calls|
|`coVerifyAll`|starts a verification block that should include all calls for coroutines|
|`verifyOrder`|starts a verification block that checks the order|
|`coVerifyOrder`|starts a verification block that checks the order for coroutines|
|`verifySequence`|starts a verification block that checks whether all calls were made in a specified sequence|
|`coVerifySequence`|starts a verification block that checks whether all calls were made in a specified sequence for coroutines|
|`excludeRecords`|exclude some calls from being recorded|
|`confirmVerified`|confirms that all recorded calls were verified|
|`clearMocks`|clears specified mocks|
|`registerInstanceFactory`|allows you to redefine the way of instantiation for certain object|
|`mockkClass`|builds a regular mock by passing the class as parameter|
|`mockkObject`|turns an object into an object mock, or clears it if was already transformed|
|`unmockkObject`|turns an object mock back into a regular object|
|`mockkStatic`|makes a static mock out of a class, or clears it if it was already transformed|
|`unmockkStatic`|turns a static mock back into a regular class|
|`clearStaticMockk`|clears a static mock|
|`mockkConstructor`|makes a constructor mock out of a class, or clears it if it was already transformed|
|`unmockkConstructor`|turns a constructor mock back into a regular class|
|`clearConstructorMockk`|clears the constructor mock|
|`unmockkAll`|unmocks object, static and constructor mocks|
|`clearAllMocks`|clears regular, object, static and constructor mocks|


### Matchers

By default, simple arguments are matched using `eq()`

|Matcher|Description|
|-------|-----------|
|`any()`|matches any argument|
|`allAny()`|special matcher that uses `any()` instead of `eq()` for matchers that are provided as simple arguments|
|`isNull()`|checks if the value is null|
|`isNull(inverse=true)`|checks if the value is not null|
|`ofType(type)`|checks if the value belongs to the type|
|`match { it.startsWith("string") }`|matches via the passed predicate|
|`coMatch { it.startsWith("string") }`|matches via the passed coroutine predicate|
|`matchNullable { it?.startsWith("string") }`|matches nullable value via the passed predicate|
|`coMatchNullable { it?.startsWith("string") }`|matches nullable value via the passed coroutine predicate|
|`eq(value)`|matches if the value is equal to the provided value via the `deepEquals` function|
|`eq(value, inverse=true)`|matches if the value is not equal to the provided value via the `deepEquals` function|
|`neq(value)`|matches if the value is not equal to the provided value via the `deepEquals` function|
|`refEq(value)`|matches if the value is equal to the provided value via reference comparison|
|`refEq(value, inverse=true)`|matches if the value is not equal to the provided value via reference comparison||
|`nrefEq(value)`|matches if the value is not equal to the provided value via reference comparison||
|`cmpEq(value)`|matches if the value is equal to the provided value via the `compareTo` function|
|`less(value)`|matches if the value is less than the provided value via the `compareTo` function|
|`more(value)`|matches if the value is more than the provided value via the `compareTo` function|
|`less(value, andEquals=true)`|matches if the value is less than or equal to the provided value via the `compareTo` function|
|`more(value, andEquals=true)`|matches if the value is more than or equal to the provided value via the `compareTo` function|
|`range(from, to, fromInclusive=true, toInclusive=true)`|matches if the value is in range via the `compareTo` function|
|`and(left, right)`|combines two matchers via a logical and|
|`or(left, right)`|combines two matchers via a logical or|
|`not(matcher)`|negates the matcher|
|`capture(slot)`|captures a value to a `CapturingSlot`|
|`capture(mutableList)`|captures a value to a list|
|`captureNullable(mutableList)`|captures a value to a list together with null values|
|`captureLambda()`|captures a lambda|
|`captureCoroutine()`|captures a coroutine|
|`invoke(...)`|calls a matched argument|
|`coInvoke(...)`|calls a matched argument for a coroutine|
|`hint(cls)`|hints the next return type in case it's gotten erased|
|`anyVararg()`|matches any elements in a vararg|
|`varargAny(matcher)`|matches if any element matches the matcher|
|`varargAll(matcher)`|matches if all elements match the matcher|
|`any...Vararg()`|matches any elements in vararg (specific to primitive type)|
|`varargAny...(matcher)`|matches if any element matches the matcher (specific to the primitive type)|
|`varargAll...(matcher)`|matches if all elements match the matcher (specific to the primitive type)|

A few special matchers available in verification mode only:

|Matcher|Description|
|-------|-----------|
|`withArg { code }`|matches any value and allows to execute some code|
|`withNullableArg { code }`|matches any nullable value and allows to execute some code|
|`coWithArg { code }`|matches any value and allows to execute some coroutine code|
|`coWithNullableArg { code }`|matches any nullable value and allows to execute some coroutine code|

### Validators

|Validator|Description|
|---------|-----------|
|`verify { mock.call() }`|Do unordered verification that a call was performed|
|`verify(inverse=true) { mock.call() }`|Do unordered verification that a call was not performed|
|`verify(atLeast=n) { mock.call() }`|Do unordered verification that a call was performed at least `n` times|
|`verify(atMost=n) { mock.call() }`|Do unordered verification that a call was performed at most `n` times|
|`verify(exactly=n) { mock.call() }`|Do unordered verification that a call was performed exactly `n` times|
|`verifyAll { mock.call1(); mock.call2() }`|Do unordered verification that only the specified calls were executed for the mentioned mocks|
|`verifyOrder { mock.call1(); mock.call2() }`|Do verification that the sequence of calls went one after another|
|`verifySequence { mock.call1(); mock.call2() }`|Do verification that only the specified sequence of calls were executed for the mentioned mocks|
|`verify { mock wasNot Called }`|Do verification that a mock was not called|
|`verify { listOf(mock1, mock2) wasNot Called }`|Do verification that a list of mocks were not called|

### Answers

An Answer can be followed up by one or more additional answers.

|Answer|Description|
|------|-----------|
|`returns value`|specify that the matched call returns a specified value|
|`returnsMany list`|specify that the matched call returns a value from the list, with subsequent calls returning the next element|
|`returnsArgument(n)`|specify that the matched call returns the nth argument of that call|
|`throws ex`|specify that the matched call throws an exception|
|`answers { code }`|specify that the matched call answers with a code block scoped with `answer scope`|
|`coAnswers { code }`|specify that the matched call answers with a coroutine code block  with `answer scope`|
|`answers answerObj`|specify that the matched call answers with an Answer object|
|`answers { nothing }`|specify that the matched call answers null|
|`just Runs`|specify that the matched call is returning Unit (returns null)|
|`propertyType Class`|specify the type of the backing field accessor|
|`nullablePropertyType Class`|specify the type of the backing field accessor as a nullable type|


### Additional answer(s)

A next answer is returned on each consequent call and the last value is persisted.
So this is similar to the `returnsMany` semantics.

|Additional answer|Description|
|------------------|-----------|
|`andThen value`|specify that the matched call returns one specified value|
|`andThenMany list`|specify that the matched call returns a value from the list, with subsequent calls returning the next element|
|`andThenThrows ex`|specify that the matched call throws an exception|
|`andThen { code }`|specify that the matched call answers with a code block scoped with `answer scope`|
|`coAndThen { code }`|specify that the matched call answers with a coroutine code block with `answer scope`|
|`andThenAnswer answerObj`|specify that the matched call answers with an Answer object|
|`andThen { nothing }`|specify that the matched call answers null|
|`andThenJust Runs`|specify that the matched call is returning Unit (available since v1.12.2)|

### Answer scope

|Parameter|Description|
|---------|-----------|
|`call`|a call object that consists of an invocation and a matcher|
|`invocation`|contains information regarding the actual function invoked|
|`matcher`|contains information regarding the matcher used to match the invocation|
|`self`|reference to the object invocation made|
|`method`|reference to the function invocation made|
|`args`|reference to the invocation arguments|
|`nArgs`|number of invocation arguments|
|`arg(n)`|nth argument|
|`firstArg()`|first argument|
|`secondArg()`|second argument|
|`thirdArg()`|third argument|
|`lastArg()`|last argument|
|`captured()`|the last element in the list for convenience when capturing to a list|
|`lambda<...>().invoke()`|call the captured lambda|
|`coroutine<...>().coInvoke()`|call the captured coroutine|
|`nothing`|null value for returning `nothing` as an answer|
|`fieldValue`|accessor to the property backing field|
|`fieldValueAny`|accessor to the property backing field with `Any?` type|
|`value`|value being set, cast to the same type as the property backing field|
|`valueAny`|value being set, with `Any?` type|
|`callOriginal`|calls the original function|

### Vararg scope

|Parameter|Description|
|---------|-----------|
|`position`|the position of an argument in a vararg array|
|`nArgs`|overall count of arguments in a vararg array|

## Funding

You can also support this project by becoming a sponsor. Your logo will show up here with a link to your website.

### Sponsors

<a href="https://opencollective.com/mockk/sponsor/0/website" target="_blank">
  <img src="https://opencollective.com/mockk/sponsor/0/avatar.svg"/>
</a>
<a href="https://opencollective.com/mockk/sponsor/1/website" target="_blank">
  <img src="https://opencollective.com/mockk/sponsor/1/avatar.svg"/>
</a>

### Backers

Thank you to all our backers! 🙏

<a href="https://opencollective.com/mockk#backers" target="_blank">
  <img src="https://opencollective.com/mockk/backers.svg?width=890"/>
</a>

### Contributors

This project exists thanks to all the people who contribute.

<a href="https://github.com/mockk/mockk/graphs/contributors">
  <img src="https://opencollective.com/mockk/contributors.svg?width=890" />
</a>

## Getting Help

To ask questions, please use Stack Overflow or Gitter.

* Chat/Gitter: [https://gitter.im/mockk-io/Lobby](https://gitter.im/mockk-io/Lobby)
* Stack Overflow: [http://stackoverflow.com/questions/tagged/mockk](http://stackoverflow.com/questions/tagged/mockk)

To report bugs, please use the GitHub project.

* Project Page: [https://github.com/mockk/mockk](https://github.com/mockk/mockk)
* Reporting Bugs: [https://github.com/mockk/mockk/issues](https://github.com/mockk/mockk/issues)

   
 
