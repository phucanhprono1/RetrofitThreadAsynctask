package com.example.retrofitthreadasynctask.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.retrofitthreadasynctask.Asynctasks.Apitask

import com.example.retrofitthreadasynctask.databinding.ActivityLoginBinding
import com.example.retrofitthreadasynctask.request.LoginRequest
import com.example.retrofitthreadasynctask.retrofit.LoginServiceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class LoginActivity : AppCompatActivity() {
    lateinit var activityLoginBinding: ActivityLoginBinding
    public var progressBar: ProgressBar? = null
    private lateinit var sharedPreferences: SharedPreferences
    val loginServiceFactory= LoginServiceFactory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding= ActivityLoginBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        setContentView(activityLoginBinding.root)
        progressBar = activityLoginBinding.progressBarLogin
        activityLoginBinding.btnLogin.setOnClickListener {
            progressBar!!.visibility = View.VISIBLE
            activityLoginBinding.btnLogin.visibility = View.GONE
            val username = activityLoginBinding.etEmail.text.toString()
            val password = activityLoginBinding.etPassword.text.toString()
            val request = LoginRequest(username, password)
            val call = loginServiceFactory.login(request)
            Apitask(this).execute(request)

        }
        activityLoginBinding.tvRegister.setOnClickListener {
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

    }
    private fun login(email: String, password: String) {
        GlobalScope.launch {
            try {
                val response =  loginServiceFactory.login(LoginRequest(email, password)).awaitResponse()

                if (response.isSuccessful) {
                    val token = response.body()!!.token
                    Log.d("LoginActivity", "Token: $token")
                    // Save the token to shared preferences
                    saveToken(token)
                    withContext(Dispatchers.Main) {
                        progressBar!!.visibility = View.GONE
                        // Start the main activity
                        startMainActivity()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar!!.visibility = View.GONE
                    // Handle login error
                    Toast.makeText(this@LoginActivity, "Login error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun saveToken(token: String?) {
        // Save the token to shared preferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun startMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}