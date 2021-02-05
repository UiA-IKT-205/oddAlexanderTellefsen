package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {
    val minToMsFactor = 60000L
    var cycleCount = 0
    val defaultCountdown = 45
    val defaultPause = 15
    lateinit var timer:CountDownTimer
    lateinit var startCountDownBt:Button
    lateinit var countDownDisplay:TextView
    lateinit var valueSeekBarCountDown: TextView
    lateinit var valueSeekBarPause: TextView
    lateinit var countDownSeekBar: SeekBar
    lateinit var pauseTimeSeekBar: SeekBar
    lateinit var pauseDisplay:TextView
    lateinit var cyclesDisplay:TextView
    lateinit var numberOfRepsEditView:EditText
    lateinit var resetBt:Button

    var timeToCountDownInMs = defaultCountdown*minToMsFactor
    var timeToPauseInMs = defaultPause*minToMsFactor
    val timeTicks = 1000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countDownSeekBar = findViewById<SeekBar>(R.id.countDownTimeSb)
        pauseTimeSeekBar = findViewById<SeekBar>(R.id.pauseTimeSb)
        valueSeekBarCountDown = findViewById(R.id.valueSeekbarCountdownTv)
        valueSeekBarPause = findViewById(R.id.valueSeekbarPauseTv)
        countDownDisplay = findViewById<TextView>(R.id.countDownView)
        pauseDisplay = findViewById(R.id.pauseView)
        cyclesDisplay = findViewById(R.id.cyclesTv)
        numberOfRepsEditView = findViewById(R.id.numberOfRepsEt)
        countDownDisplay.text = millisecondsToDescriptiveTime(countDownSeekBar.progress * minToMsFactor.toLong())
        pauseDisplay.text = millisecondsToDescriptiveTime(pauseTimeSeekBar.progress * minToMsFactor.toLong())
        valueSeekBarCountDown.text = millisecondsToDescriptiveTime(countDownSeekBar.progress * minToMsFactor.toLong())
        valueSeekBarPause.text = millisecondsToDescriptiveTime(pauseTimeSeekBar.progress * minToMsFactor.toLong())

       countDownSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
           override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                          fromUser: Boolean) {
               timeToCountDownInMs = (minToMsFactor * progress).toLong()
               valueSeekBarCountDown.text = millisecondsToDescriptiveTime(timeToCountDownInMs)
           }

           override fun onStartTrackingTouch(seekBar: SeekBar) {

           }

           override fun onStopTrackingTouch(seekBar: SeekBar) {

           }
       })

        pauseTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                           fromUser: Boolean) {
                timeToPauseInMs = (minToMsFactor * progress).toLong()
                valueSeekBarPause.text = millisecondsToDescriptiveTime(timeToPauseInMs)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

       startCountDownBt = findViewById<Button>(R.id.startCountDownBt)
       startCountDownBt.setOnClickListener(){
                        startCountDown(it)
                        updatePauseDisplay(timeToPauseInMs)
                        numberOfRepsEditView.setEnabled(false)
                        startCountDownBt.setEnabled(false)
                        countDownSeekBar.setEnabled(false)
                        pauseTimeSeekBar.setEnabled(false)
                        updateCycles(it)

       }

        resetBt = findViewById(R.id.resetBt)
        resetBt.setOnClickListener(){
            resetTimerAndButton()
        }
    }

    fun startCountDown(v: View){
        //Fikser bug med å resette timer hvis den er startet tidligere
        if(this::timer.isInitialized)
            timer.cancel()

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                startPause(v)
            }
            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }
        timer.start()
    }

    fun startPause(v: View) {
        timer = object : CountDownTimer(timeToPauseInMs,timeTicks) {
            override fun onFinish() {
                updateCycles(v)

            }
            override fun onTick(millisUntilFinished: Long) {
                updatePauseDisplay(millisUntilFinished)
            }
        }
        timer.start()

    }

    fun updateCountDownDisplay(timeInMs:Long){
        countDownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    fun updatePauseDisplay(timeInMs:Long){
        pauseDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

    private fun updateCycles(v: View) {
        if(cycleCount < numberOfRepsEditView.text.toString().toInt()) {
            cycleCount += 1
            cyclesDisplay.text = (String.format("Cycle %s of %s", cycleCount, numberOfRepsEditView.text.toString().toInt()))
            startCountDown(v)
        }
        else {
            Toast.makeText(this, R.string.complete, Toast.LENGTH_SHORT).show()
            resetTimerAndButton()
        }
    }

    fun resetTimerAndButton(){
        if(this::timer.isInitialized)
            timer.cancel()
        cycleCount=0
        timeToCountDownInMs = defaultCountdown*minToMsFactor
        timeToPauseInMs = defaultPause*minToMsFactor
        startCountDownBt.setEnabled(true)
        countDownSeekBar.setEnabled(true)
        pauseTimeSeekBar.setEnabled(true)
        numberOfRepsEditView.setEnabled(true)
        updateCountDownDisplay(timeToCountDownInMs)
        updatePauseDisplay(timeToPauseInMs)
        countDownSeekBar.progress = defaultCountdown
        pauseTimeSeekBar.progress = defaultPause
        numberOfRepsEditView.setText("1")

    }

}