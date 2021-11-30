package com.example.finalproject

import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.popup_dialog.*
import org.w3c.dom.Text

class PopupDialog(context: Context?) {
    private val dlg = Dialog(context!!)
    private lateinit var lblDesc : TextView
    private lateinit var lblDesc2 : TextView
    private lateinit var deletebtn : Button
    private lateinit var cancelbtn : Button
    private lateinit var listener : MyDialogOKClickedListener

    fun start(nickname : String, password : String) {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.popup_dialog)
        dlg.setCancelable(false)

        deletebtn = dlg.findViewById(R.id.deletebtn)
        deletebtn.setOnClickListener {

            var nick = dlg.delete_nickname.text.toString()
            var pass = dlg.delete_password.text.toString()
            if(nickname == nick && password == pass) {
                listener.onOKClicked(nick, pass, 1)
            }else{
                listener.onOKClicked(nick, pass, 0)
            }
            dlg.dismiss()
        }

        cancelbtn = dlg.findViewById(R.id.cancelbtn)
        cancelbtn.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
    fun setOnOKClickedListener(listener:(String,String,Int)->Unit){
        this.listener=object :MyDialogOKClickedListener{
            override fun onOKClicked(nick:String,pass:String,checkNum:Int){
                listener(nick,pass,checkNum)
            }
        }
    }
    interface MyDialogOKClickedListener{
        fun onOKClicked(nick:String,pass:String,checkNum:Int)
    }
}


