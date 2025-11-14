package com.example.learning3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class math : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_math)

        val btnMultiplication = findViewById<Button>(R.id.btnMultiplication)
        val btnAdditionSubtraction = findViewById<Button>(R.id.btnAdditionSubtraction)
        val btnNumbers = findViewById<Button>(R.id.btnNumbers)
        val btnInteractiveMode = findViewById<Button>(R.id.btnInteractiveMode)
        val btnFfff = findViewById<Button>(R.id.btnFfff)

        btnMultiplication.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("GAME_MODE", "MULTIPLICATION")
            startActivity(intent)
        }

        btnAdditionSubtraction.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("GAME_MODE", "ADDITION_SUBTRACTION")
            startActivity(intent)
        }

        btnNumbers.setOnClickListener {
            val intent = Intent(this, NumAction::class.java)
            startActivity(intent)
        }

        btnInteractiveMode.setOnClickListener {
            val intent = Intent(this, InteractiveModeActivity::class.java)
            startActivity(intent)
        }

        btnFfff.setOnClickListener {
            val intent = Intent(this, FfffActivity::class.java)
            startActivity(intent)
        }
    }
}