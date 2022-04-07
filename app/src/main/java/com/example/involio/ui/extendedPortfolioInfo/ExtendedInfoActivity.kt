package com.example.involio.ui.extendedPortfolioInfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import classes.network.ApiClient
import classes.network.dto.ExtendedPortfolioInfoDto
import classes.network.dto.PortfolioDto
import classes.network.utils.getJWT
import com.example.involio.R
import com.example.involio.ui.dashboard.ExchangeFragment
import com.example.involio.ui.home.CreatingPortfolioActivity
import com.example.involio.ui.home.HomeFragment
import com.example.involio.ui.notifications.SettingsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExtendedInfoActivity : AppCompatActivity() {

    private lateinit var JWT: String
    private lateinit var menuButtons: List<Button>
    lateinit var extendedInfo: ExtendedPortfolioInfoDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupExtendedInfoActivity()
        getExtendedPortfolioInfo()
    }

    private fun setupExtendedInfoActivity(){
        setContentView(R.layout.activity_extended_portfolio_info)
        JWT = getJWT(applicationContext)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = intent.extras!!.getString("namePortfolio")

        menuButtons = listOf(
            findViewById(R.id.basicInfoBut),
            findViewById(R.id.analysisBut),
            findViewById(R.id.TaxBut),
        )

        for (button in menuButtons) button.setOnClickListener {
            for (button in menuButtons) button.background = resources.getColor(R.color.button_color).toDrawable()
            it.background = resources.getColor(R.color.lightBar_color).toDrawable()
            when(it.id){
                R.id.analysisBut -> setCurrentFragment(AnalysisFragment())
                R.id.TaxBut -> setCurrentFragment(TaxFragment())
                else -> setCurrentFragment(BasicInfoFragment())
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment): Boolean{
        val backStateName = fragment.javaClass.name
        val manager: FragmentManager = supportFragmentManager
        val fragmentPopped: Boolean = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft: FragmentTransaction = manager.beginTransaction()
            ft.replace(R.id.fragment_container, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
        return true
    }

    private fun getExtendedPortfolioInfo(){
        menuButtons.forEach { it.visibility = View.GONE }
        val responseCall: Call<ExtendedPortfolioInfoDto> = ApiClient.getUserService().getExtendedPortfolioInfo(
            intent.extras!!.getInt("idPortfolio"), JWT
        )
        responseCall.enqueue(object : Callback<ExtendedPortfolioInfoDto> {
            override fun onResponse(call: Call<ExtendedPortfolioInfoDto>?, response: Response<ExtendedPortfolioInfoDto>?) {
                if (response?.isSuccessful!!) {
                    extendedInfo = response.body()!!
                    menuButtons.forEach { it.visibility = View.VISIBLE }
                    findViewById<Button>(R.id.basicInfoBut).callOnClick()
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                } else {
                    Toast.makeText(this@ExtendedInfoActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExtendedPortfolioInfoDto>?, t: Throwable?) {
                Toast.makeText(this@ExtendedInfoActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}