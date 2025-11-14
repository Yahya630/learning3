package com.example.learning3
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class myapplication : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myapplication)

        // دالة لتشغيل الصوت عند الضغط على الزر
        fun playAudio(audioResId: Int) {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this, audioResId)
            mediaPlayer.start()
        }

        val buttons = listOf(
            R.id.button1 to R.raw.audio1,
            R.id.button2 to R.raw.audio2,
            R.id.button3 to R.raw.audio4,   // تأكد أن الملف audio3 موجود
            R.id.button4 to R.raw.auido5,
            R.id.button5 to R.raw.audio6,
            R.id.button6 to R.raw.audio66,
            R.id.button7 to R.raw.auido8,
            R.id.button8 to R.raw.auido7,
            R.id.button9 to R.raw.audio9

        )




        buttons.forEach { (buttonId, audioRes) ->
            findViewById<Button>(buttonId).setOnClickListener {
                try {
                    playAudio(audioRes)
                } catch (e: Exception) {
                    Toast.makeText(this, "خطأ في تشغيل الصوت", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
