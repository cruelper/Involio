package classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.network.dto.ChangePrice
import classes.network.dto.StockInPortfolio
import com.example.involio.R

class Stock(val name: String, val ticker: String, val price: String, val num: Int,
            val diff: ChangePrice, val partInPortfolio: String, val totalPrice: String) {
    companion object {
        fun createStocksList(listOfStock: List<StockInPortfolio>, time: String): ArrayList<Stock> {
            val stocks = ArrayList<Stock>()
            for (i in listOfStock) {
                stocks.add(Stock(
                    i.name,
                    i.ticker,
                    format(i.currentUnitPrice),
                    i.count,
                    i.changePrice,
                    format(i.partOfPortfolio * 100) + "%",
                    format(i.currentUnitPrice * i.count)
                ))
            }
            return stocks
        }
    }
}

fun format(num: Double): String{
    return String.format("%.2f", num)
}

class StocksAdapter (private val mStocks: List<Stock>) : RecyclerView.Adapter<StocksAdapter.ViewHolder>()
{
    private var time: String = "year"
    private lateinit var viewHolder: ViewHolder
    private lateinit var listener: OnItemClickListener
    private lateinit var context: Context

    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun updateAll(time: String){
        this.time = time
        for (i in 0 until itemCount) notifyItemChanged(i)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.company_name)
        val tickerTextView = itemView.findViewById<TextView>(R.id.company_ticker)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StocksAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val stockView = inflater.inflate(R.layout.item_of_portfolio, parent, false)
        viewHolder = ViewHolder(stockView)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: StocksAdapter.ViewHolder, position: Int) {
        val stock: Stock = mStocks.get(position)
        viewHolder.nameTextView.text = stock.name
        viewHolder.tickerTextView.text = stock.ticker
        viewHolder.numAndPriceTextView.text = "(" + stock.num.toString() + " * " + stock.price.toString() + ")"
        viewHolder.partInPortfolioTextView.text = stock.partInPortfolio
        viewHolder.partInPortfolioTextView.minWidth = (55 * context.resources.displayMetrics.density).toInt()
        viewHolder.totalPriceTextView.text = stock.totalPrice
        viewHolder.diffTextView.text = when(time){
            "day" -> format(stock.diff.priceChangeOnDay)
            "year" -> format(stock.diff.priceChangeOnYear)
            else -> format(stock.diff.priceChangeOnAllTime)
        }
    }

    override fun getItemCount(): Int {
        return mStocks.size
    }

}