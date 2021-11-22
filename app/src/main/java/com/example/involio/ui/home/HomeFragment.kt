package com.example.involio.ui.home

import android.app.ActionBar
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.involio.R
import com.example.involio.databinding.FragmentHomeBinding
import androidx.appcompat.widget.AppCompatSpinner
import com.example.involio.BottomMenuActivity
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import classes.*
import android.view.MenuInflater




class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val actionBar: androidx.appcompat.app.ActionBar? = (activity as AppCompatActivity).supportActionBar

        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.inflate(R.menu.toolbars_element_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.select_portfolio -> {
                val portfolioSelectionFrom = PortfolioSelectionClass()
                portfolioSelectionFrom.show(parentFragmentManager, "PortfoliosSelection")
            }
            R.id.share_portfolio -> Toast.makeText(requireContext(), "делимся портфелем", Toast.LENGTH_SHORT).show()
        }
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

