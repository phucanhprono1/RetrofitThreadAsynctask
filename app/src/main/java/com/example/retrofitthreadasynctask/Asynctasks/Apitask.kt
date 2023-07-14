package com.example.retrofitthreadasynctask.Asynctasks

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.retrofitthreadasynctask.jwt.JwtManager
import com.example.retrofitthreadasynctask.request.LoginRequest
import com.example.retrofitthreadasynctask.response.AuthenticationResponse
import com.example.retrofitthreadasynctask.retrofit.LoginServiceFactory
import com.example.retrofitthreadasynctask.ui.LoginActivity
import com.example.retrofitthreadasynctask.ui.MainActivity

@Suppress("DEPRECATION")
class Apitask(private val context: Context): AsyncTask<LoginRequest, Void, AuthenticationResponse>() {

    override fun doInBackground(vararg p0: LoginRequest): AuthenticationResponse? {
        try {
            val loginService = LoginServiceFactory.create()
            val request = p0[0]
            val call = loginService.login(request)
            val response = call.execute()
            Log.d("response",call.toString())
            if(response.isSuccessful){
                val authResponse = response.body()
                return authResponse
            }
            else{
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }
    override fun onPostExecute(result: AuthenticationResponse?) {
        super.onPostExecute(result)
        val jwtManager = JwtManager.getInstance(context)
        if (result != null) {
            jwtManager.token
            if(result.success){
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                if(context is LoginActivity){
                    context.progressBar?.visibility = View.GONE
                    context.activityLoginBinding.btnLogin.visibility = View.VISIBLE
                    context.finish()

                }
            }
            else{
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                if(context is LoginActivity){
                    context.progressBar?.visibility = View.GONE
                    context.activityLoginBinding.btnLogin.visibility = View.VISIBLE
                }
            }

        }
        else{
            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            if(context is LoginActivity){
                context.progressBar?.visibility = View.GONE
                context.activityLoginBinding.btnLogin.visibility = View.VISIBLE
            }
        }
    }
}
