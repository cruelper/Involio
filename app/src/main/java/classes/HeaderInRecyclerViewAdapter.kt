package classes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import classes.network.dto.BasicPortfolioInfoDto
import classes.network.dto.ChangePrice
import classes.network.dto.PortfolioPrice
import com.example.involio.CurrenciesInExchangeActivity
import com.example.involio.R
import com.example.involio.ui.extendedPortfolioInfo.ExtendedInfoActivity

class HeaderAdapter(
    private val basicInfo: BasicPortfolioInfoDto,
    private val adapter: StocksAdapter,
    private val activity: Activity
    ): RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    private lateinit var view: View

    private var curCurrency: String = "rub"
    private var curTime: String = "year"

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val absolutPortfolioIncomeTextView: TextView = itemView.findViewById(R.id.absolut_change_data)
        private val relationPortfolioIncomeTextView: TextView = itemView.findViewById(R.id.relation_change_data)
        private val sp500IncomeTextView: TextView = itemView.findViewById(R.id.sp500_incom_data)
        private val imoexIncomeTextView: TextView = itemView.findViewById(R.id.imoex_incom_data)

        private val listOfCurrencyBut: List<Button> = listOf(
            itemView.findViewById(R.id.euro_but),
            itemView.findViewById(R.id.rubble_but),
            itemView.findViewById(R.id.usd_but),
        )

        private val listOfTimeBut: List<Button> = listOf(
            itemView.findViewById(R.id.day_but),
            itemView.findViewById(R.id.year_but),
            itemView.findViewById(R.id.allTime_but)
        )

        private val extendedInfoBut: Button = itemView.findViewById(R.id.other_info_but)

        fun bind() {
            val context: Context = activity.applicationContext
            for (i in listOfCurrencyBut) i.setOnClickListener {
                curCurrency = when (it.id) {
                    R.id.usd_but -> "usd"
                    R.id.euro_but -> "eur"
                    else -> "rub"
                }
                for (but in listOfCurrencyBut) but.background =
                    context.resources.getColor(R.color.button_color).toDrawable()
                it.background = context.resources.getColor(R.color.lightBar_color).toDrawable()
                updateData()
            }

            for (i in listOfTimeBut) i.setOnClickListener {
                curTime = when (it.id) {
                    R.id.day_but -> "day"
                    R.id.year_but -> "year"
                    else -> "all"
                }
                for (but in listOfTimeBut) but.background =
                    context.resources.getColor(R.color.button_color).toDrawable()
                it.background = context.resources.getColor(R.color.lightBar_color).toDrawable()
                updateData()
                adapter.updateAll(curTime)
            }
            listOfCurrencyBut[1].background = context.resources.getColor(R.color.lightBar_color).toDrawable()
            listOfTimeBut[1].background = context.resources.getColor(R.color.lightBar_color).toDrawable()

            extendedInfoBut.setOnClickListener {
                val intent: Intent = Intent(context, ExtendedInfoActivity::class.java)
                intent.putExtra("namePortfolio",  basicInfo.name)
                intent.putExtra("idPortfolio",  basicInfo.id)
                activity.startActivity(intent)
            }
            updateData()
        }

        fun updateData(){
            var portfolioPrice: PortfolioPrice? = null
            var imoexChange: ChangePrice? = null
            var sp500Change: ChangePrice? = null
            when (curCurrency) {
                "usd" -> {
                    portfolioPrice = basicInfo.inUSD
                    imoexChange = basicInfo.changeIMOEXInUSD
                    sp500Change = basicInfo.changeSP500InUSD
                }
                "eur" -> {
                    portfolioPrice = basicInfo.inEuro
                    imoexChange = basicInfo.changeIMOEXInEURO
                    sp500Change = basicInfo.changeSP500InEURO
                }
                else -> {
                    portfolioPrice = basicInfo.inRuble
                    imoexChange = basicInfo.changeIMOEXInRuble
                    sp500Change = basicInfo.changeSP500InRuble
                }
            }

           ( activity.findViewById<View>(R.id.total_price_data) as TextView).text =
               (String.format("%.2f", portfolioPrice!!.currentPrice) + portfolioPrice.currencySign)

            val portfolioChange: ChangePrice = portfolioPrice!!.changePrice

            when (curTime) {
                "day" -> {
                    absolutPortfolioIncomeTextView.text = String.format("%.2f", portfolioChange.priceChangeOnDay) + portfolioPrice.currencySign
                    relationPortfolioIncomeTextView.text = "(${convertDataOfHeader(portfolioPrice!!.currentPrice / portfolioChange.priceChangeOnDay)})"
                    sp500IncomeTextView.text = convertDataOfHeader(sp500Change!!.priceChangeOnDay)
                    imoexIncomeTextView.text = convertDataOfHeader(imoexChange!!.priceChangeOnDay)
                }
                "year" -> {
                    absolutPortfolioIncomeTextView.text = String.format("%.2f", portfolioChange.priceChangeOnYear) + portfolioPrice.currencySign
                    relationPortfolioIncomeTextView.text = "(${convertDataOfHeader(portfolioPrice!!.currentPrice / portfolioChange.priceChangeOnYear)})"
                    sp500IncomeTextView.text = convertDataOfHeader(sp500Change!!.priceChangeOnYear)
                    imoexIncomeTextView.text = convertDataOfHeader(imoexChange!!.priceChangeOnYear)
                }
                else -> {
                    absolutPortfolioIncomeTextView.text = String.format("%.2f", portfolioChange.priceChangeOnAllTime) + portfolioPrice.currencySign
                    relationPortfolioIncomeTextView.text = "(${convertDataOfHeader(portfolioPrice!!.currentPrice / portfolioChange.priceChangeOnAllTime)})"
                    sp500IncomeTextView.text = convertDataOfHeader(sp500Change!!.priceChangeOnAllTime)
                    imoexIncomeTextView.text =  convertDataOfHeader(imoexChange!!.priceChangeOnAllTime)
                }
            }

        }

        fun convertDataOfHeader(num: Double): String{
            return String.format("%.2f", (num - 1) * 100) + "%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.portfolios_header, parent, false)
        return HeaderViewHolder(view)
    }

    /* Binds number of flowers to the header. */
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind()
    }

    /* Returns number of items, since there is only one item in the header return one  */
    override fun getItemCount(): Int {
        return 1
    }

}