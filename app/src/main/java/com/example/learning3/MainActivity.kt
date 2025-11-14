package com.example.learning3

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learning3.math

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.background_music)
        mediaPlayer.start()

        val arabicLayout = findViewById<LinearLayout>(R.id.arb)
        arabicLayout.setOnClickListener {
            val intent = Intent(this, languagarabe::class.java)
            startActivity(intent)
        }

        val englishLayout = findViewById<LinearLayout>(R.id.eng)
        englishLayout.setOnClickListener {
            val intent = Intent(this, languagaaa::class.java)
            startActivity(intent)
        }

        val mathLayout = findViewById<LinearLayout>(R.id.math)
        mathLayout.setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            val intent = Intent(this, math::class.java)
            startActivity(intent)
        }

        val page1Layout = findViewById<LinearLayout>(R.id.rkn)
        page1Layout.setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()      // احذف هذا السطر إذا لا تريد إيقاف الموسيقى
            }
            val intent = Intent(this, page1::class.java)
            startActivity(intent)
        }

        val openLayout = findViewById<LinearLayout>(R.id.open)
        openLayout.setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()      // احذف هذا السطر إذا لا تريد إيقاف الموسيقى
            }
            val intent = Intent(this, myapplication::class.java)
            startActivity(intent)
        }
        // عنصر الألوان - يفتح شاشة ablution ويوقف الموسيقى
        findViewById<LinearLayout>(R.id.item7).setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()      // احذف هذا السطر إذا لا تريد إيقاف الموسيقى
            }
            startActivity(Intent(this, ablution::class.java))
        }
        val letterQuizLayout = findViewById<LinearLayout>(R.id.item8)
        letterQuizLayout.setOnClickListener {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()      // احذف هذا السطر إذا لا تريد إيقاف الموسيقى
            }
            val intent = Intent(this, QuizActivity::class.java) // أو QuizActivity إذا كان هذا هو اسم الصفحة
            startActivity(intent)
        }
        val flagsLayout = findViewById<LinearLayout>(R.id.item9)
        flagsLayout.setOnClickListener {
            val intent = Intent(this, Flags::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
