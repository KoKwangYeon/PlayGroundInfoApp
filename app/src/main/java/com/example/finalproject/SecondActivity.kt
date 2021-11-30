package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText

class SecondActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        init()
    }
    private fun init() {
        val num = intent.getStringExtra("btnNum").toString()


        when (num) {
            "1"->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, MainInfoFragment())
                    .commit()
            }
            "2"->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, SearchInfoFragment())
                    .commit()
            }
            "3"->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, MapFragment())
                    .commit()
            }
        }
    }

}
