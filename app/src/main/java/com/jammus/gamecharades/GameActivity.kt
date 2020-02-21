package com.jammus.gamecharades

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class GameActivity : AppCompatActivity() {

    private var word: String = ""
    private var score: Int = 0
    private var dataBase: DatabaseReference? = null
    private lateinit var wordList: MutableList<String>
    private lateinit var firebaseWordList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val gotitButton: Button = findViewById(R.id.gotitButton)
        val skipButton: Button = findViewById(R.id.skipButton)
        val endgameButton: Button = findViewById(R.id.endButton)

        dataRetrieval()
        setTimer()

        gotitButton.setOnClickListener { onCorrect() }
        skipButton.setOnClickListener { onSkip() }
        endgameButton.setOnClickListener { onGameFinish() }

    }

    private fun resetList() {

        firebaseWordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        firebaseWordList.shuffle()
    }  //used in case of internet unavailability or fire-base response fail

    private fun nextWord() {

        val wordTextView: TextView = findViewById(R.id.wordtextView)

        if (firebaseWordList.isEmpty()) {
            onGameFinish()
        } else {
            word = firebaseWordList.removeAt(0)
            wordTextView.setText("${word}")
        }

    }

    private fun onSkip() {
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        score = score.minus(1)
        if (!firebaseWordList.isEmpty()) {

            scoreTextView.setText("Score: ${score}")
        }
        nextWord()
    }

    private fun onCorrect() {
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        score = score.plus(1)

        if (!firebaseWordList.isEmpty()) {
            scoreTextView.setText("Score: ${score}")

        }
        nextWord()
    }

    private fun setTimer() {
        val timerTextView: TextView = findViewById(R.id.timerTextView)

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.setText("Remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                onGameFinish()
            }
        }.start()
    }

    private fun onGameFinish() {

        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("endscore", "${score}")
        startActivity(intent)

    }

    override fun onBackPressed() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }

    private fun dataRetrieval() {

        dataBase = FirebaseDatabase.getInstance().reference.child("randomWords")

        dataBase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val wordsReceived = dataSnapshot.getValue().toString()
                Log.d("Saad", "Value is: $wordsReceived")
                onDataRetrieved(wordsReceived)
            }

            override fun onCancelled(error: DatabaseError) { // Failed to read value
                Log.w("Saad", "Failed to read value.", error.toException())
                resetList()
            }
        })

    }

    private fun onDataRetrieved(words: String) {

        firebaseWordList = words.split(",").toMutableList()
        firebaseWordList.shuffle()
        nextWord()


    }

}




