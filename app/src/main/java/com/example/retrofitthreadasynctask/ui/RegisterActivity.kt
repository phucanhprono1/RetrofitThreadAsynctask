package com.example.retrofitthreadasynctask.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.retrofitthreadasynctask.R
import com.example.retrofitthreadasynctask.databinding.ActivityRegisterBinding
import com.example.retrofitthreadasynctask.jwt.JwtManager
import com.example.retrofitthreadasynctask.request.RegisterRequest
import com.example.retrofitthreadasynctask.retrofit.LoginService
import com.example.retrofitthreadasynctask.retrofit.LoginServiceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRegister.setOnClickListener {
            val registerRequest:RegisterRequest= RegisterRequest(
                binding.etEmailReg.text.toString(),
                binding.etPasswordReg.text.toString(),
                binding.usernameReg.text.toString(),
                if (binding.radioGroupGenderReg.checkedRadioButtonId==R.id.radioButtonNamReg) "Nam" else "Nữ",
                binding.phoneNumberReg.text.toString(),
                binding.addressReg.text.toString(),
            )
            if(binding.etPasswordReg.text.toString()!=binding.etRepasswordReg.text.toString()){
                Toast.makeText(this,"Mật khẩu không khớp",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            register(registerRequest)
        }
    }
    private fun register(registerRequest: RegisterRequest) {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    LoginServiceFactory.create().register(registerRequest)
                }

                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                    } else {
                        Toast.makeText(this@RegisterActivity, "Đăng ký thất bại do đã tồn tại email", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }
}