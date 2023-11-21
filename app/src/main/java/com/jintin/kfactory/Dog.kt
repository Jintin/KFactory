package com.jintin.kfactory

@AutoElement
class Dog(myName: String) : Animal(myName) {
    override fun sound() = "Dog($myName) sound"
}