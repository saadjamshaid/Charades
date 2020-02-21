package com.jammus.gamecharades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)


        val finalscoreTextView: TextView = findViewById(R.id.finalscoreTextView)
        val playagainButton: Button = findViewById(R.id.playagainButton)
        val score = intent.getStringExtra("endscore")

        finalscoreTextView.setText("Score: ${"$score"}")

        playagainButton.setOnClickListener { onPlayAgain() }

    }

    private fun onPlayAgain(){
        val intent = Intent (this, GameActivity::class.java)
        startActivity(intent)

    }

    override fun onBackPressed() {

        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)

    }
}


