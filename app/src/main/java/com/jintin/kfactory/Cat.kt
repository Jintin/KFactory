package com.jintin.kfactory.wrapper

import com.jintin.kfactory.Animal
import com.jintin.kfactory.AutoElement

class Wrapper {

    @AutoElement
    class Cat(extraKey: String) : Animal(extraKey) {
        override fun sound() = "Cat($myName) sound"
    }
}