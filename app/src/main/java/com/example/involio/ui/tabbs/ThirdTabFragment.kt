package com.example.involio.ui.tabbs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.involio.R
import com.example.involio.StockContentActivity

class ThirdTabFragment : Fragment() {

    private lateinit var root: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.tab_third_fragment, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stockInfo = (requireActivity() as StockContentActivity).stockInfo

        root.findViewById<TextView>(R.id.description).text = stockInfo.descriptionCompany
        root.findViewById<TextView>(R.id.country).text = stockInfo.nameCountry

        root.findViewById<ListView>(R.id.sectors).adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, stockInfo.sector
        )

        root.findViewById<ListView>(R.id.exchanges).adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, stockInfo.nameOtherExchanges
        )
    }
}