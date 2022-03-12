package com.example.involio.ui.tabbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import classes.StockDetail
import classes.StockDetailAdapter
import com.example.involio.R
import com.example.involio.StockContentActivity

class FirstTabFragment : Fragment() {

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.tab_first_fragment, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stockInfo = (requireActivity() as StockContentActivity).stockInfo
        if (stockInfo.inPortfolio.isEmpty()) root.findViewById<TextView>(R.id.noStockInPortfolioText).visibility = TextView.VISIBLE
        else{
            val rvStockInPortfolios = requireActivity().findViewById<View>(R.id.rvStockInPortfolios) as RecyclerView

            var stockInPortfolio = StockDetail
                .createStockInPortfolioList((requireActivity() as StockContentActivity).stockInfo)
            val adapter = StockDetailAdapter(stockInPortfolio)

            rvStockInPortfolios.adapter = adapter
            rvStockInPortfolios.layoutManager = LinearLayoutManager(requireActivity())
        }

    }
}