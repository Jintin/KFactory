package com.jintin.kfactory.wrapper

import com.jintin.kfactory.Animal
import com.jintin.kfactory.AutoElement

class Wrapper {

    @AutoElement("Cat")
    class Cat : Animal {
        override fun sound() = "Cat sound"
    }
}