# KFactory

[![CircleCI](https://circleci.com/gh/Jintin/KFactory.svg?style=shield)](https://app.circleci.com/pipelines/github/Jintin/KFactory)
[![Jitpack](https://jitpack.io/v/Jintin/KFactory.svg)](https://jitpack.io/#Jintin/KFactory)

KFactory is a library to auto generate simple factory classes with some annotation via ksp(Kotlin
Symbol Processing).

## Installation

First, add jitpack as one of the repositories in your project.

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

And then apply the ksp plugin in your module where you need the factory be generated.

```groovy
plugins {
    id 'com.google.devtools.ksp' version '1.6.21-1.0.5'
}
```

And then declare the dependency. Do noted that for the processor dependency, it requires `ksp`
not `kapt`

```groovy
implementation 'com.github.Jintin.KFactory:annotation:{latest-version}'
ksp 'com.github.Jintin.KFactory:processor:{latest-version}'
```

Lastly, the generated file will be located inside `build/generated/ksp/`, but your IDE might not
able to identify it. In such case you can add it manually like below:

```groovy
sourceSets {
    main {
        java {
            srcDir "${buildDir.absolutePath}/generated/ksp/"
        }
    }
}
```

## Usage

First, add `@AutoFactory` annotation to the base class that your factory class will return.
```kotlin
@AutoFactory
interface Animal { // Can be abstract class too
    fun sound(): String
}
```

And then, add `@AutoElement` annotation to the actual class you want to create with a String as a
key for factory to decide which type to create later.
```kotlin
@AutoElement("Dog")
class Dog : Animal {
    override fun sound() = "Dog sound"
}

@AutoElement("Cat")
class Cat : Animal {
    override fun sound() = "Cat sound"
}
```

After successfully compile, the {BaseClass}Factory will auto-generated like below:
```kotlin
object AnimalFactory {
  fun create(key: String): Animal = when (key) {
    "Cat" -> Cat()
    "Dog" -> Dog()
    else -> throw RuntimeException("Not support type")
  }
}
```

## Contributing

Bug reports and pull requests are welcome on GitHub at <https://github.com/Jintin/KFactory>.

## License

The module is available as open source under the terms of
the [MIT License](http://opensource.org/licenses/MIT).
