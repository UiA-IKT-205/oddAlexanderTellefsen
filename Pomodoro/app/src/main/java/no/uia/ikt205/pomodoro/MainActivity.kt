package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton30:Button
    lateinit var startButton60:Button
    lateinit var startButton90:Button
    lateinit var startButton120:Button
    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton30 = findViewById<Button>(R.id.startCountdown30Button)
       startButton30.setOnClickListener(){
           timeToCountDownInMs = 1800000
           startCountDown(it)
       }

        startButton60 = findViewById<Button>(R.id.startCountdown60Button)
        startButton60.setOnClickListener(){
            timeToCountDownInMs = 3600000
            startCountDown(it)
        }

        startButton90 = findViewById<Button>(R.id.startCountdown90Button)
        startButton90.setOnClickListener(){
            timeToCountDownInMs = 5400000
            startCountDown(it)
        }

        startButton120 = findViewById<Button>(R.id.startCountdown120Button)
        startButton120.setOnClickListener(){
            timeToCountDownInMs = 7200000
            startCountDown(it)
        }


       coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown(v: View){
        //Fikser bug med å resette timer hvis den er startet tidligere
        if(this::timer.isInitialized)
            timer.cancel()

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }
            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }
        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}