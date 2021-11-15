package com.example.loginandsignup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.loginandsignup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class signUpActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivitySignUpBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    // FireBaseAuth
    private lateinit var fireBAseAuth: FirebaseAuth

    // global values
    private var email = ""
    private var password = ""
    private var password1 = ""
    private var password2 = ""
    private var phone = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init fireBaseAuth
        fireBAseAuth = FirebaseAuth.getInstance()

        // handle click, begin SignUp
         binding.signBtn.setOnClickListener {
         //validate data
         validateData()
         }
    }
    private fun validateData() {
        //get Data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        password2 = binding.passwordsEt.text.toString().trim()
        phone = binding.phoneEt.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        }else if(!Patterns.PHONE.matcher(phone).matches()){
            //password empty
            binding.passwordEt.error = "Please enter password"
        }
        else if(TextUtils.isEmpty(password)){
            //password empty
            binding.passwordEt.error = "Please enter password"
        }else if(password.length < 6){
            //password length is less than 6
           binding.passwordEt.error = "Password must be atleast 6 characters long"

        }
        else{
            //data is validated
                fireBaseSignUpEmail()
        }
    }

    private fun fireBaseSignUpEmail() {
         //show progress
        progressDialog.show()

    // create account
        fireBAseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()

                //get current user
                val fireBaseUser = fireBAseAuth.currentUser
                val email = fireBaseUser!!.email
                Toast.makeText(this, "Account created Successfully with email $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener {  e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun fireBaseSignUpPhone() {
        //show progress
        progressDialog.show()

        // create account

        fireBAseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()

                //get current user
                val fireBaseUser = fireBAseAuth.currentUser
                val email = fireBaseUser!!.email
                Toast.makeText(this, "Account created Successfully with email $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener {  e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
       onBackPressed() //go back to provide activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }
}