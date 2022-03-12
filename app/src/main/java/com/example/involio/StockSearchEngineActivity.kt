package com.example.involio

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import classes.network.ApiClient
import classes.network.dto.PortfolioDto
import classes.network.dto.SearchedElement
import classes.network.utils.getJWT
import com.example.involio.databinding.ActivityMainBinding
import com.example.involio.databinding.ActivityStockSearchEngineBinding
import com.example.involio.ui.home.CreatingPortfolioActivity
import com.example.involio.ui.home.HomeFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.DialogFragment
import java.io.Serializable


class StockSearchEngineActivity: AppCompatActivity() {

    lateinit var binding: ActivityStockSearchEngineBinding

    var responseMap: MutableMap<String, List<SearchedElement>> = mutableMapOf()
    var pageNumDefaultStocks: Int = 0
    var pageNumSearchedStocks: Int = 0
    var defaultStocks: MutableList<String> = mutableListOf()
    var curStocks: MutableList<String> = mutableListOf()
    lateinit var stocksAdapter: ArrayAdapter<String>
    var isDefaultStocksActive: Boolean = true

    lateinit var portfoliosId: IntArray
    lateinit var portfoliosName: Array<String>
    lateinit var portfoliosDates: LongArray

    val pageSize = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockSearchEngineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        portfoliosId = intent.extras!!.getIntArray("portfoliosId")!!
        portfoliosName = intent.extras!!.getStringArray("portfoliosName")!!
        portfoliosDates = intent.extras!!.getLongArray("portfoliosDates")!!

        setListeners()
        getAllStockByPages()
    }

    private fun setListeners(){
        val searchView = binding.searchView

        stocksAdapter = ArrayAdapter(
            this, R.layout.searched_company, curStocks
        )

        val stockList = findViewById<ListView>(R.id.stockList)
        stockList.adapter = stocksAdapter
        stockList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            val myDialogFragment = ChoiceExchangeToStockFragment(selectedItem, responseMap[selectedItem]!!, portfoliosId, portfoliosName, portfoliosDates)
            val manager = supportFragmentManager
            myDialogFragment.show(manager, "myDialog")
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                pageNumSearchedStocks = 0
                getStockByQuery(query!!)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)){
                    binding.moreBut.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.INVISIBLE
                    stocksAdapter.clear()
                    stocksAdapter.addAll(defaultStocks)
                    stocksAdapter.filter.filter("")
                    isDefaultStocksActive = true

                }
                return false
            }
        })

        binding.moreBut.setOnClickListener {
            if (isDefaultStocksActive) getAllStockByPages()
            else getStockByQuery(binding.searchView.query.toString())
        }
    }

    private fun getAllStockByPages(){
        isDefaultStocksActive = true
        val responseCall: Call<Map<String, List<SearchedElement>>> = ApiClient.getUserService().getAllStock(pageNumDefaultStocks, getJWT(applicationContext))
        responseCall.enqueue(object : Callback<Map<String, List<SearchedElement>>> {
            override fun onResponse(call: Call<Map<String, List<SearchedElement>>>?, response: Response<Map<String, List<SearchedElement>>>?) {
                if (response?.isSuccessful!!) {
                    binding.moreBut.visibility = View.INVISIBLE
                    binding.emptyView.visibility = View.VISIBLE
                    binding.moreBut.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.INVISIBLE

                    pageNumDefaultStocks++
                    responseMap.putAll(response.body()!!)
                    defaultStocks.addAll(response.body()!!.keys)
                    stocksAdapter.clear()
                    stocksAdapter.addAll(defaultStocks)

                    stocksAdapter.filter.filter("")
                    binding.moreBut.visibility = if (response.body()!!.size < pageSize) View.INVISIBLE else  View.VISIBLE
                } else {
                    Toast.makeText(this@StockSearchEngineActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, List<SearchedElement>>>?, t: Throwable?) {
                Toast.makeText(this@StockSearchEngineActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getStockByQuery(query: String){
        isDefaultStocksActive = false
        val responseCall: Call<Map<String, List<SearchedElement>>> = ApiClient.getUserService().searchStock(
            query, pageNumSearchedStocks, getJWT(applicationContext)
        )
        responseCall.enqueue(object : Callback<Map<String, List<SearchedElement>>> {
            override fun onResponse(call: Call<Map<String, List<SearchedElement>>>?, response: Response<Map<String, List<SearchedElement>>>?) {
                if (response?.isSuccessful!!) {
                    pageNumSearchedStocks++
                    responseMap.putAll(response.body()!!)
                    if (pageNumSearchedStocks == 1) stocksAdapter.clear()
                    stocksAdapter.addAll(response.body()!!.keys)

                    stocksAdapter.filter.filter("")
                    binding.moreBut.visibility = if (response.body()!!.size < pageSize) View.INVISIBLE else  View.VISIBLE
                    binding.emptyView.visibility = if (response.body()!!.isEmpty() && pageNumSearchedStocks == 1) View.VISIBLE else View.INVISIBLE
                } else {
                    Toast.makeText(this@StockSearchEngineActivity, "Ошибка при получении данных!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, List<SearchedElement>>>?, t: Throwable?) {
                Toast.makeText(this@StockSearchEngineActivity, t?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

}

class ChoiceExchangeToStockFragment(
    private val companyName: String, private var stocksData: List<SearchedElement>,
    private val portfoliosId: IntArray, private val portfoliosName: Array<String>,
    private val portfoliosDates: LongArray
    ) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(companyName)
                .setItems(stocksData.map { stock -> stock.ticker + " (" + stock.nameExchange + ')'}.toTypedArray()
                ) { dialog, which ->
                    Toast.makeText(activity, "Выбранный тикер: ${stocksData[which].ticker}", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(requireActivity(), StockContentActivity::class.java)
                    intent.putExtra("nameCompany", companyName)
                    intent.putExtra("ticker", stocksData[which].ticker)
                    intent.putExtra("idExchange", stocksData[which].idExchange)
                    intent.putExtra("nameExchange", stocksData[which].nameExchange)
                    intent.putExtra("portfoliosId", portfoliosId)
                    intent.putExtra("portfoliosName", portfoliosName)
                    intent.putExtra("portfoliosDates", portfoliosDates)
                    startActivity(intent)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}