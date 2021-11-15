package com.example.loginandsignup

import android.app.ProgressDialog
import android.content.ContentValues.TAG
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

class VerifyActivity : AppCompatActivity() {
    // global values
    private var otp1 = ""
    private var otp2 = ""
    private var otp3 = ""
    private var otp4 = ""
    private var otp5 = ""
    private var otp6 = ""
    private var otpValid: Boolean = true

    //ViewBinding
    private lateinit var binding: ActivityVerifyBinding

    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    // FireBaseAuth
    private lateinit var fireBAseAuth: FirebaseAuth

    // PhoneAuthCredentials
    private lateinit var phoneAuthCredential: PhoneAuthCredential

    // PhoneAuthCredentials
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    // PhoneAuthCredentials
    private lateinit var nCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    // Verified ID
    private var storedVerificationId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init fireBaseAuth
        fireBAseAuth = FirebaseAuth.getInstance()


        //get Data
        otp1 = binding.otpNumberOne.text.toString().trim()
        otp2 = binding.optNumberTwo.text.toString().trim()
        otp3 = binding.otpNumberThree.text.toString().trim()
        otp4 = binding.otpNumberFour.text.toString().trim()
        otp5 = binding.otpNumberFive.text.toString().trim()
        otp6 = binding.otpNumberSix.text.toString().trim()

        //validate data
        validateData(otpNumberOne)
        validateData(binding.optNumberTwo)
        validateData(binding.otpNumberThree)
        validateData(binding.otpNumberFour)
        validateData(binding.otpNumberFive)
        validateData(binding.otpNumberSix)

        nCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "OTP Verification Failed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                super.onCodeSent(verificationId,token)

                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                binding.resendOTP.setVisibility(View.GONE)
            }

            override fun onCodeAutoRetrievalTimeOut(s: String) {
                super.onCodeAutoRetrievalTimeOut(s)
                binding.resendOTP.setVisibility(View.VISIBLE)
            }

//            private  fun sendOTP(phoneNumber: String){
//                PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60,TimeUnit.SECONDS, this@VerifyActivity, nCallbacks)
//            }

private fun startPhoneNumberVerification(phoneNumber: String) {
    // [START start_phone_auth]
    val options = PhoneAuthOptions.newBuilder(fireBAseAuth)
        .setPhoneNumber(phoneNumber)       // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(this@VerifyActivity)       // Activity (for callback binding)
        .setCallbacks(nCallbacks)          // OnVerificationStateChangedCallbacks
        .build()
    PhoneAuthProvider.verifyPhoneNumber(options)
    // [END start_phone_auth]
}

            private fun resendVerificationCode(
                phoneNumber: String,
                token: PhoneAuthProvider.ForceResendingToken?
            ) {
                val optionsBuilder = PhoneAuthOptions.newBuilder(fireBAseAuth)
                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this@VerifyActivity)                 // Activity (for callback binding)
                    .setCallbacks(nCallbacks)          // OnVerificationStateChangedCallbacks
                if (token != null) {
                    optionsBuilder.setForceResendingToken(token) // callback's ForceResendingToken
                }
                PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
            }


        }


        if(otpValid){

        }

    }



    private fun validateData(field: EditText){
if(field.text.isEmpty()){
    field.error = "Required"
    otpValid = false
}else{
    otpValid = true
}
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        fireBAseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
}