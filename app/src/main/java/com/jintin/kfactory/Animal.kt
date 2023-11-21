package com.jintin.kfactory

@AutoFactory
abstract class Animal(val myName: String) { // Can be interface too if you don't need shared parameter
    abstract fun sound(): String
}
