package com.example.involio.ui.tabbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.Contact
import classes.ContactsAdapter
import classes.StockDetail
import classes.StockDetailAdapter
import com.example.involio.R

class FirstTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tab_first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvStockInPortfolios = requireActivity().findViewById<View>(R.id.rvStockInPortfolios) as RecyclerView
        var stockInfo = StockDetail.createStockInPortfolioList(5)
        val adapter = StockDetailAdapter(stockInfo)

        rvStockInPortfolios.adapter = adapter
        rvStockInPortfolios.layoutManager = LinearLayoutManager(requireActivity())
    }
}