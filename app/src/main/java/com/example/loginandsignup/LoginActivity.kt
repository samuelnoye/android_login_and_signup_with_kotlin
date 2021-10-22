package com.example.loginandsignup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.loginandsignup.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding:ActivityLoginBinding

    // ActionBAr
    private lateinit var actionBar: ActionBar

    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    // FireBaseAuth
    private lateinit var fireBAseAuth: FirebaseAuth

    // global values
    private var email = ""
    private var password = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init fireBaseAuth
        fireBAseAuth = FirebaseAuth.getInstance()
        checkUser()

        // handle click, open register activity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, signUpActivity::class.java))
        }


        // handle click, begin login
       binding.loginBtn.setOnClickListener {
       // before logging in, validate data
          validateData()
       }

    }

    private fun validateData() {
         //get Data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            //password empty
            binding.passwordEt.error = "Please enter password"
        }else{
           //data is validated
            fireBaseLogin()
        }
    }

    private fun fireBaseLogin() {
         // show progress
        progressDialog.show()
        fireBAseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()

                //get user info
                val fireBaseUser = fireBAseAuth.currentUser
                val email = fireBaseUser!!.email
                Toast.makeText(this,"logged as $email", Toast.LENGTH_SHORT).show()

                //open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()

            }
            .addOnFailureListener { e->
            // login failed
           progressDialog.dismiss()
           Toast.makeText(this,"login failed due to${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user is already logged in, goto profile activity
        //get current user
        val fireBaseUser = fireBAseAuth.currentUser
        if(fireBaseUser != null){
            //user is already logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}