package com.jintin.kfactory.wrapper

import com.jintin.kfactory.Animal
import com.jintin.kfactory.AutoElement

@AutoElement("Fish")
class Fish : Animal {
    override fun sound() = "NA"
}