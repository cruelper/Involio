package classes

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.involio.SignInActivity
import android.content.Context

import android.view.ViewGroup

import android.view.LayoutInflater
import android.widget.TextView
import com.example.involio.LogInActivity
import com.example.involio.R
import com.example.involio.StockContentActivity
import com.example.involio.ui.tabbs.PlaceholderFragment


class PortfolioSelectionClass : DialogFragment() {
    private val portfoliosName = arrayOf("Дивидендный","Акции роста", "Европейские акции", "Биокеки")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Выберете портфель")
                    .setItems(portfoliosName) {dialog, which -> setPortfolioContents(which)}
                    .setNeutralButton("Создать новый портфель")
                    { dialog, id -> Toast.makeText(activity, "Перенаправление на создание портфеля", Toast.LENGTH_SHORT).show() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    public fun setPortfolioContents(id: Int){
//        val viewFragmentHome = requireActivity().findViewById<View>(R.id.fragment_home_view) as ViewGroup
//        val inflater = requireActivity().applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val viewTotalPrice = inflater.inflate(R.layout.total_price_portfolios, viewFragmentHome)

        val viewTotalPrice = requireActivity().findViewById<View>(R.id.total_price_data) as TextView
        viewTotalPrice.text = "3228$"

        (activity as AppCompatActivity).supportActionBar?.title = portfoliosName[id]

        val rvContacts = requireActivity().findViewById<View>(R.id.rvContacts) as RecyclerView
        val headerAdapter = HeaderAdapter()
        var contacts = Contact.createContactsList(20)
        val adapter = ContactsAdapter(contacts)
        val curActivity = requireActivity()

        adapter.setOnItemClickListener(object : ContactsAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                val name = contacts[position].name
                Toast.makeText(curActivity, "открывается вкладка с инфой по $name", Toast.LENGTH_SHORT).show()
                if (itemView != null) {
                    itemView.context.startActivity(Intent(itemView.context, StockContentActivity::class.java))
                }
            }
        })

        rvContacts.adapter = ConcatAdapter(headerAdapter, adapter)
        rvContacts.layoutManager = LinearLayoutManager(requireActivity())
        Toast.makeText(activity, "Выбран портфель: ${portfoliosName[id]}", Toast.LENGTH_SHORT).show()
    }

}