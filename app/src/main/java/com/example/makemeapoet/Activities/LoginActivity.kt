package com.example.makemeapoet.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.makemeapoet.Home
import com.example.makemeapoet.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    val ZAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LogBtn.setOnClickListener {
            val email = LogMail.text.toString()
            val password = LogPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Log.d("LoginActivity", "emptiness works${it}")

                Toast.makeText(this,"Please enter email/pasword", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)return@addOnCompleteListener
                    Log.d("Login_Activity", "Successfully signed in ${it.result.user.uid}")
                    SendToHomeActivity()
                }
                .addOnFailureListener {
                    Log.d("Login_Activity", "Not signed in ${it.message}")
                }



        }

        back_to_register_activity.setOnClickListener{
            finish()
        }
    }





    private fun SendToHomeActivity() {

        val d = Intent(this.applicationContext, Home::class.java)
        startActivity(d)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val mFirebaseUser = ZAuth.currentUser

        if (mFirebaseUser != null)
        {
            SendToHomeActivity()
        }
    }



}
