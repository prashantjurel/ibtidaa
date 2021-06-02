package com.prashant.ibtidaa.common.loginSignup

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prashant.ibtidaa.HomeActivity
import com.prashant.ibtidaa.R

class LoginActivity : AppCompatActivity() {
    private lateinit var emailAddress: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var emailAddressText: TextInputEditText
    private lateinit var passwordText: TextInputEditText
    private lateinit var progressBar: LinearLayout
    private lateinit var loginBtn: MaterialButton
    private lateinit var mAwesomeValidation: AwesomeValidation
    private lateinit var forgotPassword: TextView
    private lateinit var signUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailAddress = findViewById(R.id.loginEmailAddress)
        emailAddressText = findViewById(R.id.loginEmailAddressInputText)
        password = findViewById(R.id.loginPassword)
        passwordText = findViewById(R.id.loginPasswordInputText)
        progressBar = findViewById(R.id.loginProgressBar)
        loginBtn = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        signUp = findViewById(R.id.signUpForFree)
        mAwesomeValidation = AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT)
        mAwesomeValidation.setTextInputLayoutErrorTextAppearance(R.style.error_appearance)

        //Add Validations for text Fields Before Submission
        mAwesomeValidation.addValidation(this, R.id.loginEmailAddress, Patterns.EMAIL_ADDRESS, R.string.err_email)
        val regexPassword = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"
        mAwesomeValidation.addValidation(this, R.id.loginPassword, regexPassword, R.string.err_loginPassword)

        //On click function on login button
        loginBtn.setOnClickListener { letTheUserLoggedIn() }

        //On click function on buttons or links
        forgotPassword.setOnClickListener { startActivity(Intent(applicationContext, ForgotPasswordActivity::class.java)) }
        emailAddress.setOnClickListener { mAwesomeValidation.clear() }
        emailAddressText.setOnClickListener { mAwesomeValidation.clear() }
        password.setOnClickListener { mAwesomeValidation.clear() }
        passwordText.setOnClickListener { mAwesomeValidation.clear() }
        signUp.setOnClickListener{ startActivity(Intent(applicationContext,SignUpScreenActivity::class.java)) }
    }

    private fun letTheUserLoggedIn() {
        if (!isConnectedToInternet(this@LoginActivity)) {
            showConnectToInternetDialog()
        }
        if (mAwesomeValidation.validate()) {
            progressBar.visibility = View.VISIBLE
            //get data
            var emailAddressInput = emailAddress.editText!!.text.toString().trim { it <= ' ' }
            val passwordInput = password.editText!!.text.toString().trim { it <= ' ' }
            emailAddressInput = encodeUserEmail(emailAddressInput)


            //set data in database
            val checkUserExists = FirebaseDatabase.getInstance().getReference("Users").orderByChild("emailAddress").equalTo(emailAddressInput)
            val finalEmailAddressInput = emailAddressInput
            checkUserExists.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        emailAddress.error = null
                        emailAddress.isErrorEnabled = false
                        val systemPassword = snapshot.child(finalEmailAddressInput).child("password").getValue(String::class.java)
                        if (systemPassword == passwordInput) {
                            password.error = null
                            password.isErrorEnabled = false
                            progressBar.visibility = View.GONE
                            btnEnabled()
                            startActivity(Intent(applicationContext, HomeActivity::class.java))
                            Toast.makeText(this@LoginActivity, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                        } else {
                            progressBar.visibility = View.GONE
                            btnEnabled()
                            Toast.makeText(this@LoginActivity, "Password does not match with our records", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        mAwesomeValidation.clear()
                        btnEnabled()
                        Toast.makeText(this@LoginActivity, "Email is not registered with us.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    progressBar.visibility = View.GONE
                    mAwesomeValidation.clear()
                    btnEnabled()
                    Toast.makeText(this@LoginActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun redirectToSignup() {
        val intent = Intent(applicationContext, SignUpScreenActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showConnectToInternetDialog() {
        val builder = AlertDialog.Builder(this@LoginActivity, R.style.MyDialogTheme)
        builder.setMessage("Please connect to internet to proceed further.")
                .setCancelable(false)
                .setPositiveButton("Connect") { _, _ -> startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)) }
                .setNegativeButton("Cancel") { _, _ ->
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun btnEnabled() {
        emailAddress.isEnabled = true
        password.isEnabled = true
        forgotPassword.isEnabled = true
        signUp.isEnabled = true
    }


    companion object {
        @JvmStatic
        fun encodeUserEmail(userEmail: String): String {
            return userEmail.replace(".", ",")
        }

        /*Checking For Internet Connection*/
        @JvmStatic
        fun isConnectedToInternet(context: Context): Boolean {
            val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            return activeNetwork != null
        }
    }
}