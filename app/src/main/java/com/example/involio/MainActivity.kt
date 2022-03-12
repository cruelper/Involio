package com.example.involio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    public fun goto_SignIn(view: View){
        val signIn_intent = Intent(this, SignInActivity::class.java)
        startActivity(signIn_intent)
    }

    public fun goto_LogIn(view: View){
        val logIn_intent = Intent(this, LogInActivity::class.java)
        startActivity(logIn_intent)
    }

    public fun goto_Work(view: View){
        val logIn_intent = Intent(this, BottomMenuActivity::class.java)
        startActivity(logIn_intent)
    }

}