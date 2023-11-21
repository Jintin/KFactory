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
    id 'com.google.devtools.ksp' version '1.9.10-1.0.13'
}
```

And then declare the dependency. Do noted that for the processor dependency, it requires `ksp`
not `kapt`

```groovy
implementation 'com.github.Jintin.KFactory:annotation:{latest-version}'
ksp 'com.github.Jintin.KFactory:processor:{latest-version}'
```

## Usage

First, add `@AutoFactory` annotation to the base class that your factory class will return.
```kotlin
@AutoFactory
interface Animal { // Can be abstract class too
    fun sound(): String
}
```

And then, add `@AutoElement` annotation to the actual class you want to create.
```kotlin
@AutoElement
class Dog : Animal {
    override fun sound() = "Dog sound"
}

@AutoElement
class Cat : Animal {
    override fun sound() = "Cat sound"
}
```

After successfully compile, the `AnimalFactory.kt` will auto-generated like below:
```kotlin
public enum class AnimalType {
    CAT,
    DOG,
}

public fun AnimalFactory(key: AnimalType): Animal = when (key) {
    AnimalType.CAT -> Cat()
    AnimalType.DOG -> Dog()
}
```

Now you can call `AnimalFactory(AnimalType.CAT)` to get a cat or passing `AnimalType.DOG` to get a dog instead.

## Contributing

Bug reports and pull requests are welcome on GitHub at <https://github.com/Jintin/KFactory>.

## License

The module is available as open source under the terms of
the [MIT License](http://opensource.org/licenses/MIT).
