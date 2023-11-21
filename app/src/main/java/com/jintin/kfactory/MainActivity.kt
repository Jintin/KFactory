package com.jintin.kfactory

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.jintin.kfactory.databinding.ActivityMainBinding

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            dog.setOnClickListener {
                toast(AnimalFactory(AnimalType.DOG, "SNOOPY"))
            }
            cat.setOnClickListener {
                toast(AnimalFactory(AnimalType.CAT, "KITTY"))
            }
            fish.setOnClickListener {
                toast(AnimalFactory(AnimalType.FISH, "MIMO"))
            }
        }
    }

    private fun toast(animal: Animal) {
        Toast.makeText(this@MainActivity, animal.sound(), Toast.LENGTH_SHORT).show()
    }

}