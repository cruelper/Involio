package classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.network.dto.CurrencyDto
import com.example.involio.R

class CurrencyInExchangeAdapter (private val mCurrencies: List<Pair<CurrencyDto, Double>>) :
    RecyclerView.Adapter<CurrencyInExchangeAdapter.ViewHolder>() {

    private lateinit var viewHolder: ViewHolder
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateAll(){
        for (i in 0 until itemCount) notifyItemChanged(i)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.company_name)
        val tickerTextView = itemView.findViewById<TextView>(R.id.company_ticker)
        val priceTextView = itemView.findViewById<TextView>(R.id.total_price)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(itemView, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyInExchangeAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val currencyView = inflater.inflate(R.layout.item_of_portfolio, parent, false)
        viewHolder = ViewHolder(currencyView)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: CurrencyInExchangeAdapter.ViewHolder, position: Int) {
        val currency: Pair<CurrencyDto, Double> = mCurrencies[position]
        viewHolder.nameTextView.text = currency.first.name
        viewHolder.tickerTextView.text = currency.first.id
        viewHolder.priceTextView.text = String.format("%.2f", currency.second) + '₽'
    }

    override fun getItemCount(): Int {
        return mCurrencies.size
    }

}