package com.example.involio.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.involio.R
import androidx.appcompat.app.AppCompatActivity
import classes.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.network.ApiClient
import classes.network.dto.*
import classes.network.utils.getJWT
import com.example.involio.BottomMenuActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.TextView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.involio.StockContentActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject

class HomeFragment : Fragment() {

    var root: View? = null
    lateinit var portfolios: List<PortfolioDto>
    private var curTime: String = "all"
    private var curCurrency: String = "rub"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false)

        val toolbar: Toolbar = root!!.findViewById(R.id.toolbar_with_spinner)
        val spinner: AppCompatSpinner = root!!.findViewById(R.id.spinner_in_toolbar)
        val curActivity = (requireActivity() as AppCompatActivity)
        curActivity.setSupportActionBar(toolbar)
        curActivity.supportActionBar!!.setDisplayShowHomeEnabled(false)
        curActivity.supportActionBar!!.setDisplayShowTitleEnabled(false)
        portfolios = (requireActivity() as BottomMenuActivity).getPortfolios()
        val spinnerAdapter = object : ArrayAdapter<String>(
            requireActivity(), R.layout.portfolios_spinner_item, portfolios.map { it.name } + "Создать портфель"
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val v = super.getDropDownView(position, convertView, parent)
                if (position == portfolios.size)
                    (v as TextView).setTextColor(resources.getColor(R.color.darkBar_color))
                return v
            }
        }

        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == portfolios.size) {
                    Toast.makeText(activity, "Перенаправление на создание портфеля", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this@HomeFragment.requireActivity(), CreatingPortfolioActivity::class.java)
                    intent.putExtra("isNoOnePortfolio", false)
                    startActivity(intent)
                }
                else {
                    root!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE
                    setPortfolioContents(position)
                }
                Toast.makeText(
                    requireActivity(),
                    "Выбран какой-то портфель!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return root
    }


    fun setPortfolioContents(id: Int) {
        val portfolioInfoResponseCall: Call<BasicPortfolioInfoDto> =
            ApiClient.getUserService().getBasicPortfolioInfo(
                portfolios[id].id, getJWT(requireActivity().applicationContext), "No"
            )
        portfolioInfoResponseCall.enqueue(object : Callback<BasicPortfolioInfoDto> {
            override fun onResponse(
                call: Call<BasicPortfolioInfoDto>?,
                response: Response<BasicPortfolioInfoDto>?
            ) {
                if (response?.isSuccessful!!) {
                    root!!.findViewById<ConstraintLayout>(R.id.total_price_bar).visibility = View.VISIBLE
                    setAdapters(response.body()!!)
                    Toast.makeText(activity, "Выбран портфель: ${portfolios[id].name}", Toast.LENGTH_SHORT).show()
                    root!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                } else {
                    Toast.makeText(activity, "Ошибка при получении данных!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<BasicPortfolioInfoDto>?, t: Throwable?) {
                Toast.makeText(activity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAdapters(basicPortfolioInfoDto: BasicPortfolioInfoDto) {
        val rvStocks = requireActivity().findViewById<View>(R.id.rvContacts) as RecyclerView
        val stocks = Stock.createStocksList(basicPortfolioInfoDto.stocksInPortfolio!!, curTime)
        val adapter = StocksAdapter(stocks)
        adapter.setOnItemClickListener(object : StocksAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val intent = Intent(requireActivity(), StockContentActivity::class.java)
                intent.putExtra("nameCompany", stocks[position].name)
                intent.putExtra("ticker", stocks[position].ticker)
                intent.putExtra("idExchange", basicPortfolioInfoDto.stocksInPortfolio[position].idExchange)
                intent.putExtra("nameExchange", basicPortfolioInfoDto.stocksInPortfolio[position].nameExchange)
                intent.putExtra("portfoliosId", portfolios.map { it.id }.toIntArray())
                intent.putExtra("portfoliosName", portfolios.map { it.name }.toTypedArray())
                intent.putExtra("portfoliosDates", portfolios.map { it.dataOfCreation }.toLongArray())
                itemView?.context?.startActivity(intent)
            }
        })


        val headerAdapter = HeaderAdapter(basicPortfolioInfoDto, adapter, requireActivity())

        rvStocks.adapter = ConcatAdapter(headerAdapter, adapter)
        rvStocks.layoutManager = LinearLayoutManager(requireActivity())
    }
}
