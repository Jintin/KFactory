package com.jintin.kfactory.wrapper

import com.jintin.kfactory.Animal
import com.jintin.kfactory.AutoElement

@AutoElement
class Fish(extraKey: String) : Animal(extraKey) {
    override fun sound() = "Fish($myName) no sound"
}