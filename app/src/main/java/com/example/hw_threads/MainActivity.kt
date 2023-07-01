package com.example.hw_threads

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var integers: List<Int>? = null
    var progressbar: ProgressBar? = null
    var status: TextView? = null
    var startButton: Button? = null
    var cancelButton: Button? = null
    var progressTask: ProgressTask? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        integers = (0..100).toList()
        progressbar = findViewById(R.id.progbar)
        startButton = findViewById<Button?>(R.id.button_start)
        cancelButton = findViewById<Button?>(R.id.button_cancel)
        status = findViewById(R.id.status)
        progressTask = ProgressTask(integers!!, progressbar!!, status!!, startButton!!)
        cancelButton?.also {
            it.setOnClickListener({
                status?.text = "Pending"
                if(!progressTask?.isCancelled!!){
                    progressTask?.cancel(true)
                }
                startButton?.isClickable = true
            })
        }
        startButton?.also {
            it.setOnClickListener({
                status?.text = "Running"
                if(!progressTask?.isCancelled!! && progressTask?.status != AsyncTask.Status.FINISHED){
                    progressTask?.execute()
                }
                else if(progressTask?.status == AsyncTask.Status.FINISHED || progressTask?.isCancelled!!) {
                    progressbar?.progress = 0
                    progressTask = ProgressTask(integers!!, progressbar!!, status!!, startButton!!)
                    progressTask?.execute()
                }
                it.isClickable = false
            })

        }
    }

    class ProgressTask(val integers: List<Int>, val progressBar: ProgressBar, val statusTV: TextView, val startButton: Button) :
        AsyncTask<Void?, Int, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            for (i in integers) {
                publishProgress(i)
                SystemClock.sleep(300)
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            progressBar.progress = values[0]!!.plus(1)
        }

        override fun onPostExecute(result: Void?) {
            statusTV.text = "Finished"
            startButton.isClickable = true
        }
    }


}