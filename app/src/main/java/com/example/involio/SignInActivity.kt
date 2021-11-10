package com.example.involio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }
}