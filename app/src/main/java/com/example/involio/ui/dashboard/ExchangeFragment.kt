package com.example.involio.ui.dashboard

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.involio.R
import com.example.involio.databinding.FragmentExchangeBinding

class ExchangeFragment : Fragment() {
// для поиска
    var stocks: List<String> = listOf("Apple", "AbbVie", "Delta Air Lines", "DR Horton Inc.", "Exxon Mobil Corp.", "Gap Inc.", "Kroger Co.", "Merck & Co. Inc.", "Галя", "Тал любимый")
    var arrayAdapter: ArrayAdapter<String>? = null
// для поиска

    private lateinit var exchangeViewModel: ExchangeViewModel
    private var _binding: FragmentExchangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        exchangeViewModel = ViewModelProvider(this).get(ExchangeViewModel::class.java)
        _binding = FragmentExchangeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val ll = inflater.inflate(R.layout.fragment_exchange, container, false)

        arrayAdapter = ArrayAdapter<String>(ll.context, android.R.layout.simple_list_item_1, stocks)
        val a = (ll.findViewById(R.id.search_listview) as ListView)
        a.adapter = arrayAdapter

        val actionBar: androidx.appcompat.app.ActionBar? = (activity as AppCompatActivity).supportActionBar

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.search_menu, menu)

        val menuItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Apple, Tal, USD"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                arrayAdapter?.filter?.filter(newText)
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}