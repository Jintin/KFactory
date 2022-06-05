package com.jintin.kactory

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val input = findViewById<TextView>(R.id.input)
        val text = findViewById<TextView>(R.id.sound)
        button.setOnClickListener {
            val animal = AnimalFactory.create(input.text.toString())
            text.text = animal.sound()
        }

    }
}