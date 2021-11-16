package com.example.loginandsignup

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.loginandsignup.databinding.ActivitySignUpBinding
import com.example.loginandsignup.databinding.ActivityVerifyBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit
import android.widget.Toast

import com.google.firebase.auth.AuthResult

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.PhoneAuthCredential

import com.google.firebase.auth.PhoneAuthProvider

import androidx.annotation.NonNull

import android.R
import android.widget.Button

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks


class VerifyActivity : AppCompatActivity() {
    // global values
    private var getOtpNumberOne = ""
    private var getOtpNumberTwo = ""
    private var getOtpNumberThree = ""
    private var getOtpNumberFour = ""
    private var getOtpNumberFive = ""
    private var getOtpNumberSix = ""
    var verifyPhone: Button? = null
    var resendOTP:Button? = null

    var otpValid = true
    var firebaseAuth: FirebaseAuth? = null
    private lateinit var binding: ActivityVerifyBinding
    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog
    var phoneAuthCredential: PhoneAuthCredential? = null
    var token: ForceResendingToken? = null
    var verificationId: String? = null
    var phone: String? = null
    var mCallbacks: OnVerificationStateChangedCallbacks? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent
        phone = data.getStringExtra("phone")

        firebaseAuth = FirebaseAuth.getInstance()

        getOtpNumberOne = binding.otpNumberOne.text.toString().trim()
        getOtpNumberTwo = binding.optNumberTwo.text.toString().trim()
        getOtpNumberThree = binding.otpNumberThree.text.toString().trim()
        getOtpNumberFour = binding.otpNumberFour.text.toString().trim()
        getOtpNumberFive = binding.otpNumberFive.text.toString().trim()
        getOtpNumberSix = binding.otpNumberSix.text.toString().trim()


        verifyPhone = binding.verifyPhoneBTn
        resendOTP = binding.resendOTP

        binding.verifyPhoneBTn.setOnClickListener(View.OnClickListener {
            validateField(otpNumberOne)
            validateField(optNumberTwo)
            validateField(otpNumberThree)
            validateField(otpNumberFour)
            validateField(otpNumberFive)
            validateField(otpNumberSix)

            if (otpValid) {
                // send otp to the user
                val otp = getOtpNumberOne+getOtpNumberTwo+getOtpNumberThree+getOtpNumberFour+
                          getOtpNumberFive+getOtpNumberSix

                val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                verifyAuthentication(credential)
            }
        })
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
                token = forceResendingToken
                binding.resendOTP.visibility = View.GONE
            }

            override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                binding.resendOTP.visibility = View.VISIBLE
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                verifyAuthentication(phoneAuthCredential)
                binding.resendOTP.visibility = View.GONE
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@VerifyActivity, "OTP Verification Failed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        sendOTP(phone)
        binding.resendOTP.setOnClickListener { resendOTP(phone) }
    }

   private fun sendOTP(phoneNumber: String?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber!!, 60, TimeUnit.SECONDS, this,
            mCallbacks!!
        )
    }

    private fun resendOTP(phoneNumber: String?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber!!, 60, TimeUnit.SECONDS, this,
            mCallbacks!!, token
        )
    }


    private fun validateField(field: EditText?) {
        if (field!!.text.toString().isEmpty()) {
            field.error = "Required"
            otpValid = false
        } else {
            otpValid = true
        }
    }

    fun verifyAuthentication(credential: PhoneAuthCredential?) {
        firebaseAuth!!.currentUser!!.linkWithCredential(credential!!).addOnSuccessListener {
            Toast.makeText(this@VerifyActivity, "Acccount Created and Linked.", Toast.LENGTH_SHORT)
                .show()
            // send to dashboard.
        }
    }
}