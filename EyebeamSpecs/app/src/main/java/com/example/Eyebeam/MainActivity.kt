package com.example.Eyebeam

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.Eyebeam.HomeActivity
import com.example.Eyebeam.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(2000)
        installSplashScreen()

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val videoView: VideoView = findViewById<VideoView>(R.id.bgvideo)
        val videoPath = "android.resource://com.example.eyebeam/" + R.raw.firstbgvideo
        val uri = Uri.parse(videoPath)

        videoView.setVideoURI(uri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        videoView.start()

        val btnst = findViewById<Button>(R.id.btngetstart)

        btnst.setOnClickListener {
            val intval = Intent(this@MainActivity,RegisterActivity::class.java)
            startActivity(intval)

        }
    }
}