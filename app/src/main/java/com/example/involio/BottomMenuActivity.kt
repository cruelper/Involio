package com.example.involio

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import classes.network.ApiClient
import classes.network.dto.PortfolioDto
import classes.network.utils.getJWT
import com.example.involio.ui.dashboard.ExchangeFragment
import com.example.involio.ui.home.CreatingPortfolioActivity
import com.example.involio.ui.home.HomeFragment
import com.example.involio.ui.notifications.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class BottomMenuActivity : AppCompatActivity() {

    private lateinit var JWT: String
    private lateinit var navController: BottomNavigationView
    private lateinit var portfolios: List<PortfolioDto>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setupBottomMenuActivity()
        startWork()
    }

    fun getPortfolios() = portfolios

    private fun setupBottomMenuActivity(){
        setContentView(R.layout.activity_bottom_menu)

        navController = findViewById<BottomNavigationView>(R.id.nav_view)
        navController.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.navigation_home -> setCurrentFragment(HomeFragment())
                R.id.navigation_dashboard -> setCurrentFragment(ExchangeFragment())
                R.id.navigation_notifications -> setCurrentFragment(SettingsFragment())
                else -> false
            }
        }
        JWT = getJWT(applicationContext)
    }

    private fun setCurrentFragment(fragment: Fragment): Boolean{
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.home_fragment_container, fragment)
//            commit()
//        }
//        return true
        val backStateName = fragment.javaClass.name
        val manager: FragmentManager = supportFragmentManager
        val fragmentPopped: Boolean = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.add(R.id.home_fragment_container, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
        return true
    }

    private fun startWork(){
        val portfolioResponseCall: Call<List<PortfolioDto>> = ApiClient.getUserService().getUserPortfolios(JWT)
        portfolioResponseCall.enqueue(object : Callback<List<PortfolioDto>> {
            override fun onResponse(call: Call<List<PortfolioDto>>?, response: Response<List<PortfolioDto>>?) {
                if (response?.isSuccessful!!) {
                    if (response.body()?.size == 0){
                        Toast.makeText(this@BottomMenuActivity, "Созданных портфелей нет. Перенаправление на создание портфеля.", Toast.LENGTH_SHORT).show()
                        val intent: Intent = Intent(this@BottomMenuActivity, CreatingPortfolioActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("isNoOnePortfolio", true)
                        startActivity(intent)
                    } else{
                        Toast.makeText(this@BottomMenuActivity, "Портфели есть, открываем окно выбора портфелей", Toast.LENGTH_SHORT).show()
                        portfolios = response.body()!!
                        setCurrentFragment(HomeFragment())
                    }
                } else {
                    Toast.makeText(this@BottomMenuActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PortfolioDto>>?, t: Throwable?) {
                Toast.makeText(this@BottomMenuActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
