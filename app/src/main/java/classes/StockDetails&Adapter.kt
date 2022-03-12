package classes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import classes.network.dto.StockInfoDto
import com.example.involio.LogInActivity
import com.example.involio.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class StockDetail(val nameOfPortfolio: String, val percentageOnPortfolio: String, val stockTotalPrice: String, val priceChange: String,
                  val meanPrice: String, val purchases: List<String>) {
    companion object {
        fun createStockInPortfolioList(stockInfo: StockInfoDto): ArrayList<StockDetail> {
            fun format(num: Double): String = String.format("%.2f",num)
            val portfolios = ArrayList<StockDetail>()
            val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            for (i in stockInfo.inPortfolio) {
                val curTotalPrice = i.countInPortfolio * stockInfo.currentPrice
                val purchaseTotalPrice = i.averagePurchasePrice*i.countInPortfolio
                val sign = stockInfo.currencySign
                portfolios.add(StockDetail(
                    i.namePortfolio,
                    format(i.partOfPortfolio * 100) + "% от",
                    format(curTotalPrice) + sign + ": ${i.countInPortfolio} шт * ${stockInfo.currentPrice}",
                    format(curTotalPrice - purchaseTotalPrice) + sign + " (${format((curTotalPrice / purchaseTotalPrice - 1) * 100)}%)",
                    format(i.averagePurchasePrice) + sign,
                    i.purchases.map { "${SimpleDateFormat("dd/MM/yyy").format(Date(it.first))} - ${it.second} * ${format(it.third)} $sign" }
                ))


//                portfolios.add(StockDetail("Дивидендный", "13.2% от", "777$: 5 акций * 168.3$", "+32$ (+3.4%)",
//                    "136.3$", listOf("22.12.2003 - 6 * 111$","12.10.2005 - 6 * 111$","12.10.2005 - 6 * 111$","12.10.2005 - 6 * 111$","12.10.2005 - 6 * 111$")))
            }
            return portfolios
        }
    }
}

class StockDetailAdapter (private val mStockDetail: List<StockDetail>) : RecyclerView.Adapter<StockDetailAdapter.ViewHolder>()
{
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private lateinit var listener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfPortfolioTextView = itemView.findViewById<TextView>(R.id.nameOfPortfolio)
        val percentageOnPortfolioTextView = itemView.findViewById<TextView>(R.id.percentageOnPortfolio)
        val stockTotalPriceTextView = itemView.findViewById<TextView>(R.id.stockTotalPrice)
        val priceChangeTextView = itemView.findViewById<TextView>(R.id.priceChange)
        val meanPriceTextView = itemView.findViewById<TextView>(R.id.meanPrice)
        val purchasesLinearLayout = itemView.findViewById<LinearLayout>(R.id.listOfPurchase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockDetailAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val stockInPortfolioView = inflater.inflate(R.layout.item_stock_in_portfolio_detail, parent, false)
        return ViewHolder(stockInPortfolioView)
    }

    override fun onBindViewHolder(viewHolder: StockDetailAdapter.ViewHolder, position: Int) {
        val stockDetail: StockDetail = mStockDetail.get(position)
        viewHolder.nameOfPortfolioTextView.text = stockDetail.nameOfPortfolio
        viewHolder.percentageOnPortfolioTextView.text = stockDetail.percentageOnPortfolio
        viewHolder.stockTotalPriceTextView.text = stockDetail.stockTotalPrice
        viewHolder.priceChangeTextView.text = stockDetail.priceChange
        viewHolder.meanPriceTextView.text = stockDetail.meanPrice

        for (purchase in stockDetail.purchases){
            val context = viewHolder.purchasesLinearLayout.context
            val newPurchase: TextView = TextView(context)
            newPurchase.text = purchase
            newPurchase.setTextColor(context.getColor(R.color.text_color))
            newPurchase.textSize = 16f
            viewHolder.purchasesLinearLayout.addView(newPurchase)
        }
    }

    override fun getItemCount(): Int {
        return mStockDetail.size
    }
}