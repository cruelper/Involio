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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

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
                if (getPortfolios()[position] == "Добавить портфель"){
                    Toast.makeText(requireContext(), "Тут будет создание портфеля", Toast.LENGTH_SHORT).show()


                }
                else{
                    Toast.makeText(requireContext(), "Тут будет инфа по портфелю " + getPortfolios()[position], Toast.LENGTH_SHORT).show()
                    val rvContacts = requireActivity().findViewById<View>(R.id.rvContacts) as RecyclerView
                    // Initialize contacts
                    var contacts = Contact.createContactsList(20)
                    // Create adapter passing in the sample user data
                    val adapter = ContactsAdapter(contacts)
                    // Attach the adapter to the recyclerview to populate items
                    rvContacts.adapter = adapter
                    // Set layout manager to position the items
                    rvContacts.layoutManager = LinearLayoutManager(requireActivity())
                    // That's all!
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
class Contact(val name: String, val isOnline: Boolean) {

    companion object {
        private var lastContactId = 0
        fun createContactsList(numContacts: Int): ArrayList<Contact> {
            val contacts = ArrayList<Contact>()
            for (i in 1..numContacts) {
                contacts.add(Contact("Person " + ++lastContactId, i <= numContacts / 2))
            }
            return contacts
        }
    }
}

class ContactsAdapter (private val mContacts: List<Contact>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.contact_name)
        val messageButton = itemView.findViewById<Button>(R.id.message_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.item_contact, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: ContactsAdapter.ViewHolder, position: Int) {
        val contact: Contact = mContacts.get(position)
        val textView = viewHolder.nameTextView
        textView.setText(contact.name)
        val button = viewHolder.messageButton
        button.text = if (contact.isOnline) "Message" else "Offline"
        button.isEnabled = contact.isOnline
    }

    override fun getItemCount(): Int {
        return mContacts.size
    }
}
