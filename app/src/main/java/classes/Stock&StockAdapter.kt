package classes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.involio.LogInActivity
import com.example.involio.R

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