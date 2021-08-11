package com.example.makemeapoet.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.makemeapoet.Home
import com.example.makemeapoet.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        regBtn.setOnClickListener {
            performRegister()





        }

        already_have_an_account.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        select_photo.setOnClickListener {
            Log.d("Register_Activity", "show the freaking photo")




            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    var selected_photo_uri:Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data !=null)
        {
            Log.d("Register_Activity", "Photo was selected")

             selected_photo_uri =data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selected_photo_uri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            select_photo.setBackgroundDrawable(bitmapDrawable)

        }
    }

    private fun performRegister() {
        val email = regMail.text.toString()
        val password = regPassword.text.toString()

        if (email.isEmpty() && password.isEmpty())
        {
            Toast.makeText(this,"Please enter email/pasword",Toast.LENGTH_SHORT).show()
            return


        }


        Log.d("Register_Activity", "Email is: " + email)
        Log.d("Register_Activity", "Password is: " + password)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("Register_Activity", "Successfully created user with uid: ${it.result.user.uid}")

                uploadIMageTOFirebaseStorage()

            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to create new user ${it.message}",Toast.LENGTH_SHORT).show()

                Log.d("Register_Activity", "Failed to create user ${it.message}")
            }
    }


    private fun uploadIMageTOFirebaseStorage() {

        if (selected_photo_uri == null ) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")

        ref.putFile(selected_photo_uri!!)
            .addOnSuccessListener {
                Log.d("Register_Activity", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnCompleteListener {
                    //it.toString()
                    Log.d("Register_Activity", "File Location $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
                    .addOnFailureListener {

                    }
            }

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user =
            User(uid, regName.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register_Activity", "Congrats, Saved User To firebaseDatabase")
                updateUI()
            }

    }

    private fun updateUI() {

        val intent = Intent(this, Home::class.java)
        startActivity(intent)

    }
}

class User(val uid: String,val username : String, val profileImageUrl:String)
