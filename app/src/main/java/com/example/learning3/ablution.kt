package com.example.learning3
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class ablution : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ablution)

        fun playAudio(audioResId: Int) {
            if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
            mediaPlayer = MediaPlayer.create(this, audioResId)
            mediaPlayer.start()
        }

        val buttons = listOf(
            Pair(R.id.button1, R.raw.a1),
            Pair(R.id.button2, R.raw.a2),
            Pair(R.id.button3, R.raw.a3),
            Pair(R.id.button4, R.raw.a4),
            Pair(R.id.button5, R.raw.a5),
            Pair(R.id.button7, R.raw.a6),
            Pair(R.id.button8, R.raw.a7),
            Pair(R.id.button9, R.raw.a8),
            Pair(R.id.button10, R.raw.a9),
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
