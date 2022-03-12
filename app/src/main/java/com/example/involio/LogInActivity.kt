package com.example.involio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import android.text.TextUtils
import android.widget.Toast
import classes.network.ApiClient
import classes.network.dto.AuthRequest
import classes.network.dto.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.SharedPreferences





class LogInActivity : AppCompatActivity() {

    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var butLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        loginInitialization()
    }

    fun loginInitialization(){
        username = findViewById(R.id.login_username_text_field)
        password = findViewById(R.id.login_password_text_field)
        butLogin = findViewById(R.id.but_login)

        if (intent.extras != null) username.setText(intent.getStringExtra("username"))

        butLogin.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(username.text.toString()) || TextUtils.isEmpty(password.text.toString()))
                Toast.makeText(this, "Логин или пароль не введены", Toast.LENGTH_SHORT).show()
            else{
                login()
            }
        })
    }

    fun login(){
        var authRequest: AuthRequest = AuthRequest(
            login = username.text.toString(),
            password = password.text.toString()
        )
        var loginResponseCall: Call<AuthResponse> = ApiClient.getUserService().userAuth(authRequest)
        loginResponseCall.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>?, response: Response<AuthResponse>?) {
                if (response?.isSuccessful!!) {
                    val pref = applicationContext.getSharedPreferences("MyPref", 0)
                    val editor: SharedPreferences.Editor = pref.edit()
                    editor.putString("JWT", response.body()?.jwtToken)
                    editor.apply()

                    Toast.makeText(this@LogInActivity, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this@LogInActivity, BottomMenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LogInActivity, "Логин или пароль введены не верно!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>?, t: Throwable?) {
                Toast.makeText(this@LogInActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}