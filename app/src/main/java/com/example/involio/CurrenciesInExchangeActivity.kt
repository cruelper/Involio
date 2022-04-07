package com.example.involio

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.CurrencyInExchangeAdapter
import classes.HeaderAdapter
import classes.Stock
import classes.StocksAdapter
import classes.network.ApiClient
import classes.network.dto.BasicPortfolioInfoDto
import classes.network.dto.CurrencyDto
import classes.network.utils.getJWT
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrenciesInExchangeActivity: AppCompatActivity()  {

    lateinit var portfoliosId: IntArray
    lateinit var portfoliosName: Array<String>
    lateinit var portfoliosDates: LongArray

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_currencies_in_exchange)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        this.setSupportActionBar(toolbar)
        this.supportActionBar!!.setDisplayShowHomeEnabled(true)
        this.supportActionBar!!.title = "Валюта"

        portfoliosId = intent.extras!!.getIntArray("portfoliosId")!!
        portfoliosName = intent.extras!!.getStringArray("portfoliosName")!!
        portfoliosDates = intent.extras!!.getLongArray("portfoliosDates")!!

        setCurrencies()
    }

    private fun setCurrencies(){
        val currencyResponseCall: Call<List<Pair<CurrencyDto, Double>>> =
            ApiClient.getUserService().getAllCurrencyWithPrice(getJWT(applicationContext))
        currencyResponseCall.enqueue(object : Callback<List<Pair<CurrencyDto, Double>>> {
            override fun onResponse(
                call: Call<List<Pair<CurrencyDto, Double>>>?,
                response: Response<List<Pair<CurrencyDto, Double>>>?
            ) {
                if (response?.isSuccessful!!) {
                    Toast.makeText(this@CurrenciesInExchangeActivity, "Список валют успешно загружен", Toast.LENGTH_SHORT).show()
                    setAdapters(response.body()!!)
                    findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE

                } else {
                    Toast.makeText(this@CurrenciesInExchangeActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pair<CurrencyDto, Double>>>?, t: Throwable?) {
                Toast.makeText(this@CurrenciesInExchangeActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapters(currencies: List<Pair<CurrencyDto, Double>>){
        val rvCurrencies = findViewById<View>(R.id.rvCurrencies) as RecyclerView
        val adapter = CurrencyInExchangeAdapter(currencies)
        adapter.setOnItemClickListener(object : CurrencyInExchangeAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val name = currencies[position].first.name
                Toast.makeText(
                    this@CurrenciesInExchangeActivity,
                    "открывается вкладка с инфой по $name",
                    Toast.LENGTH_SHORT
                ).show()
                val intent: Intent = Intent(this@CurrenciesInExchangeActivity, CurrencyContentActivity::class.java)
                val currencyDto = currencies[position].first
                intent.putExtra("nameCurrency", currencyDto.name)
                intent.putExtra("id", currencyDto.id)
                intent.putExtra("portfoliosId", portfoliosId)
                intent.putExtra("portfoliosName", portfoliosName)
                intent.putExtra("portfoliosDates", portfoliosDates)
                startActivity(intent)
            }
        })
        rvCurrencies.adapter = adapter
        rvCurrencies.layoutManager = LinearLayoutManager(this@CurrenciesInExchangeActivity)
    }

}