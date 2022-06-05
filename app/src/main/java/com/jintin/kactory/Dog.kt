package com.jintin.kactory

import com.jintin.kactory.AutoElement

@AutoElement("Dog")
class Dog : Animal {
    override fun sound() = "Dog sound"
}