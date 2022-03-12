package com.example.involio.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import classes.network.dto.PortfolioDto
import com.example.involio.BottomMenuActivity
import com.example.involio.CurrenciesInExchangeActivity
import com.example.involio.R
import com.example.involio.StockSearchEngineActivity
import com.example.involio.databinding.FragmentExchangeBinding
import com.example.involio.ui.home.CreatingPortfolioActivity
import java.io.Serializable

class ExchangeFragment : Fragment() {
    var root: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_exchange, container, false)
        setListeners()

        val toolbar: Toolbar = root!!.findViewById(R.id.toolbar)
        val curActivity = (requireActivity() as AppCompatActivity)
        curActivity.setSupportActionBar(toolbar)
        curActivity.supportActionBar!!.setDisplayShowHomeEnabled(false)
        curActivity.supportActionBar!!.title = "Биржа"


        return root
    }

    private fun setListeners(){
        val toSearchStockBut = root?.findViewById(R.id.stocks_but) as Button
        toSearchStockBut.setOnClickListener {
            val intent: Intent = Intent(requireActivity(), StockSearchEngineActivity::class.java)
            val portfolios = (requireActivity() as BottomMenuActivity).getPortfolios()
            val portfoliosId = portfolios.map { it.id }.toIntArray()
            val portfoliosName = portfolios.map { it.name }.toTypedArray()
            val portfoliosDateOfCreation = portfolios.map { it.dataOfCreation }.toLongArray()
            intent.putExtra("portfoliosId",  portfoliosId)
            intent.putExtra("portfoliosName",  portfoliosName)
            intent.putExtra("portfoliosDates",  portfoliosDateOfCreation)
            startActivity(intent)
        }

        val toCurrencyBut = root?.findViewById(R.id.currency_but) as Button
        toCurrencyBut.setOnClickListener {
            val intent: Intent = Intent(requireActivity(), CurrenciesInExchangeActivity::class.java)
            startActivity(intent)
        }

        val toBookmarksBut = root?.findViewById(R.id.bookmarks_but) as Button
        toBookmarksBut.setOnClickListener {

        }
    }
}