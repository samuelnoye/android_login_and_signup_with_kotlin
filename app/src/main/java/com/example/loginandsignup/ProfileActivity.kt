package com.example.loginandsignup

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.loginandsignup.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

//ViewBinding
private lateinit var binding: ActivityProfileBinding

//ActionBar
private lateinit var actionBar1: ActionBar

// FireBaseAuth
private lateinit var fireBaseAuth: FirebaseAuth

// ProgressDialog
private lateinit var progressDialog: ProgressDialog

private var firstName = ""
private var secondName = ""
private var email = ""

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar1 = supportActionBar!!
        actionBar1.title = "login"

        //init fireBaseAuth
        fireBaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait ")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            fireBaseAuth.signOut()
            checkUser()
        }
        binding.submitBtn.setOnClickListener {
            //start progress dialog
            progressDialog.show()

            //get data
            email = binding.emailTv.text.toString().trim()
            firstName = binding.firstNameEt.text.toString().trim()
            secondName = binding.secondNameEt.text.toString().trim()

            if(firstName.isEmpty() && secondName.isEmpty()){
                Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show()
            }else{
                //end progress dialog
                progressDialog.dismiss()

                FirebaseDatabase.getInstance().getReference().child("person").push().child("firstName").setValue(firstName)
                FirebaseDatabase.getInstance().getReference().child("person").push().child("secondName").setValue(secondName)
                FirebaseDatabase.getInstance().getReference().child("person").push().child("email").setValue(email)


            }

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