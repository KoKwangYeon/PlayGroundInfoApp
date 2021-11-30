package com.example.finalproject

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.File
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)







        init()

    }

    private fun init() {


        infobtn.setOnClickListener {




            val i = Intent(this,SecondActivity::class.java)
            i.putExtra("btnNum","1")
            startActivity(i)
            overridePendingTransition(0,0)
        }
        searchinfobtn.setOnClickListener {
            val i = Intent(this,SecondActivity::class.java)
            i.putExtra("btnNum","2")
            startActivity(i)
            overridePendingTransition(0,0)
        }
    }

}

