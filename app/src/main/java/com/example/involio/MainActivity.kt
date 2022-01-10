package com.example.involio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.MainAdapter
import classes.network.*
import classes.network.models.MyUser
import classes.network.Status.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide();
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        setupObservers()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        )[MainViewModel::class.java]
    }

    private fun setupUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        viewModel.getUsers().observe(this, Observer {
            it?.let { resource ->
                when (resource.status) {
                    SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resource.data?.let { users -> retrieveList(users) }
                    }
                    ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(users: List<MyUser>) {
        adapter.apply {
            addUsers(users)
            notifyDataSetChanged()
        }
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