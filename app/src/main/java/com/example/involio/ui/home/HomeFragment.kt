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
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView

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

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater){
        menuInflater.inflate(R.menu.portfilios_menu, menu)

        val item = menu?.findItem(R.id.portfolios_spinner)
        val spinner = item?.actionView as AppCompatSpinner

        var arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.layout_portfolios_drop_title, getPortfolios())
        arrayAdapter.setDropDownViewResource(R.layout.layout_portfolios_drop_list)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val rvContacts = requireActivity().findViewById<View>(R.id.rvContacts) as RecyclerView
                if (getPortfolios()[position] == "Добавить портфель"){
                    Toast.makeText(requireContext(), "Тут будет создание портфеля", Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(requireContext(), "Тут будет инфа по портфелю " + getPortfolios()[position], Toast.LENGTH_SHORT).show()
                    var contacts = Contact.createContactsList(20)
                    val adapter = ContactsAdapter(contacts)

                    adapter.setOnItemClickListener(object : ContactsAdapter.OnItemClickListener {
                        override fun onItemClick(itemView: View?, position: Int) {
                            val name = contacts[position].name
                            Toast.makeText(requireContext(), "открывается вкладка с инфой по $name", Toast.LENGTH_SHORT).show()
                        }
                    })

                    rvContacts.adapter = adapter
                    rvContacts.layoutManager = LinearLayoutManager(requireActivity())
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun getPortfolios(): MutableList<String> {
        val mdList = mutableListOf<String>()
        var portfolios = listOf<String>("Дивидендный","Акции роста", "Европейские акции", "Добавить портфель")
        for (portfolio in portfolios) {
            mdList.add(portfolio)
        }
        return mdList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//класс для вывода акций
class Contact(val name: String, val price: String, val num: Int,
              val diff: String, val partInPortfolio: Float, val totalPrice: String) {
    companion object {
        fun createContactsList(numContacts: Int): ArrayList<Contact> {
            val contacts = ArrayList<Contact>()
            for (i in 1..numContacts) {
                contacts.add(Contact("AAPL", "148.3$", 3, "+12.3%", 53f, "444.9$"))
            }
            return contacts
        }
    }
}

class ContactsAdapter (private val mContacts: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()
{
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.company_name)
        val numAndPriceTextView = itemView.findViewById<TextView>(R.id.num_and_price)
        val partInPortfolioTextView = itemView.findViewById<TextView>(R.id.part_in_portfolio)
        val totalPriceTextView = itemView.findViewById<TextView>(R.id.total_price)
        val diffTextView = itemView.findViewById<TextView>(R.id.diff)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_stock, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: ContactsAdapter.ViewHolder, position: Int) {
        val contact: Contact = mContacts.get(position)
        viewHolder.nameTextView.setText(contact.name)
        viewHolder.numAndPriceTextView.setText(contact.num.toString() + " * " + contact.price.toString())
        viewHolder.partInPortfolioTextView.setText(contact.partInPortfolio.toString() + "%")
        viewHolder.totalPriceTextView.setText(contact.totalPrice)
        viewHolder.diffTextView.setText(contact.diff)
    }

    override fun getItemCount(): Int {
        return mContacts.size
    }
}
