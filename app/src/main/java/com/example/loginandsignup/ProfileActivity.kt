package com.example.loginandsignup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.loginandsignup.databinding.ActivityProfileBinding
import com.example.loginandsignup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

//ViewBinding
private lateinit var binding: ActivityProfileBinding

//ActionBar
private lateinit var actionBar: ActionBar

// FireBaseAuth
private lateinit var fireBaseAuth: FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "login"

        //init fireBaseAuth
        fireBaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            fireBaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
         // check user is logged in or not
        val firebaseUser = fireBaseAuth.currentUser
        if(firebaseUser != null){
            // user not null, user is not logged in, get user info
            val email = firebaseUser.email
            //set to text view
            binding.emailTv.text = email

        }
        else{
            // user not null, user is not logged in, goto login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}