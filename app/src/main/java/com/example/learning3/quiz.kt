package com.example.learning3

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.learning3.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var mediaPlayers: List<MediaPlayer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // تفعيل ViewBinding
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // تحميل ملفات الصوت
        mediaPlayers = listOf(
            MediaPlayer.create(this, R.raw.q1),
            MediaPlayer.create(this, R.raw.q2),
            MediaPlayer.create(this, R.raw.q3),
            MediaPlayer.create(this, R.raw.q4),
            MediaPlayer.create(this, R.raw.q5),
            MediaPlayer.create(this, R.raw.q6),
            MediaPlayer.create(this, R.raw.q7),
            MediaPlayer.create(this, R.raw.q8),
            MediaPlayer.create(this, R.raw.q9),
            MediaPlayer.create(this, R.raw.q10)
        )

        // ربط كل زر بالصوت المناسب
        val buttons = listOf(
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6,
            binding.button7,
            binding.button8,
            binding.button9,
            binding.button10
        )

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                mediaPlayers.forEach { mp ->
                    if (mp.isPlaying) {
                        mp.pause()
                        mp.seekTo(0)
                    }
                }
                mediaPlayers[index].start()
            }
        }

        // لتكييف المحتوى مع الحواف (اختياري)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // تحرير الموارد عند إغلاق الصفحة
        mediaPlayers.forEach { it.release() }
    }
}
