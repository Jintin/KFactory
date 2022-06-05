package com.jintin.kactory.wrapper

import com.jintin.kactory.Animal
import com.jintin.kactory.AutoElement

class Wrapper {

    @AutoElement("Cat")
    class Cat : Animal {
        override fun sound() = "Cat sound"
    }
}