package com.jintin.kactory.wrapper

import com.jintin.kactory.Animal
import com.jintin.kactory.AutoElement

@AutoElement("Fish")
class Fish : Animal {
    override fun sound() = "NA"
}