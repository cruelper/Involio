package com.example.involio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast
import classes.network.ApiClient
import classes.network.dto.AuthRequest
import classes.network.dto.AuthResponse
import classes.network.dto.MyUserDto
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent




class SignInActivity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var butRegistration: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        registrationInitialization()
    }

    fun registrationInitialization(){
        email = findViewById(R.id.signin_email_text_field)
        username = findViewById(R.id.signin_username_text_field)
        password = findViewById(R.id.signin_password_text_field)
        butRegistration = findViewById(R.id.but_signin)

        butRegistration.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(username.text.toString()) || TextUtils.isEmpty(password.text.toString()) || TextUtils.isEmpty(email.text.toString()))
                Toast.makeText(this, "Электронная почта, логин или пароль не введены", Toast.LENGTH_SHORT).show()
            else{
                registration()
            }
        })
    }

    fun registration(){
        var registrationRequest: MyUserDto = MyUserDto(
            email =email.text.toString(),
            login = username.text.toString(),
            password = password.text.toString()
        )
        var registrationResponseCall: Call<Pair<String, Boolean>> = ApiClient.getUserService().userRegistration(registrationRequest)
        registrationResponseCall.enqueue(object : Callback<Pair<String, Boolean>> {
            override fun onResponse(call: Call<Pair<String, Boolean>>?, response: Response<Pair<String, Boolean>>?) {
                if (response?.isSuccessful!! && response.body()!!.second) {
                    val intent = Intent(this@SignInActivity, LogInActivity::class.java)
                    intent.putExtra("username", username.text.toString());
                    startActivity(intent)
                    finish()
                }
                Toast.makeText(this@SignInActivity, response.body()!!.first, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Pair<String, Boolean>>?, t: Throwable?) {
                Toast.makeText(this@SignInActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}