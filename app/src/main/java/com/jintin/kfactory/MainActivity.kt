package com.jintin.kfactory

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.jintin.kfactory.databinding.ActivityMainBinding

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            dog.setOnClickListener {
                toast(AnimalFactory(AnimalType.DOG))
            }
            cat.setOnClickListener {
                toast(AnimalFactory(AnimalType.CAT))
            }
            fish.setOnClickListener {
                toast(AnimalFactory(AnimalType.FISH))
            }
        }
    }

    private fun toast(animal: Animal) {
        Toast.makeText(this@MainActivity, animal.sound(), Toast.LENGTH_SHORT).show()
    }

}