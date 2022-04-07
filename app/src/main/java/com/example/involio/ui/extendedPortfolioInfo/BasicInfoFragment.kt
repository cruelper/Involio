package com.example.involio.ui.extendedPortfolioInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import classes.network.dto.BasicValues
import classes.network.dto.ExtendedPortfolioInfoDto
import classes.network.utils.*
import com.example.involio.R
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BasicInfoFragment : Fragment() {
    private lateinit var root: View
    private lateinit var extendedInfo: ExtendedPortfolioInfoDto
    private var curCurrency: String = "rub"
    private var curTime: String = "month"
    private lateinit var mChart: CombinedChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        extendedInfo = (requireActivity() as ExtendedInfoActivity).extendedInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_basic_portfolio_info, container, false)

        root.findViewById<TextView>(R.id.nameBroker).text = extendedInfo.nameBroker
        root.findViewById<TextView>(R.id.typeOfBrokerAccount).text = extendedInfo.nameTypeOfBrokerAccount

        setupCombinedChart()
        setupCurrencyBut()
        setupTimeBut()

        root.findViewById<Button>(R.id.rub).callOnClick()
        root.findViewById<Button>(R.id.monthBut).callOnClick()

        return root
    }

    private fun setupCombinedChart(){
        mChart = root.findViewById(R.id.chart) as CombinedChart
        setupChart(mChart, requireContext())

        val priceView = root.findViewById<TextView>(R.id.infoOnClick)
        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                val formatter =  when(curTime){
                    "month" -> SimpleDateFormat("yyyy-MM-dd")
                    "year" -> SimpleDateFormat("yyyy-MM-dd")
                    else -> SimpleDateFormat("yyyy-MM")
                }
                val intervalInCurrency = when(curCurrency){
                    "rub" -> extendedInfo.rubInterval
                    "eur" -> extendedInfo.euroInterval
                    else -> extendedInfo.usdInterval
                }
                var curInterval = when(curTime){
                    "month" -> intervalInCurrency.monthInterval
                    "year" -> intervalInCurrency.yearInterval
                    else -> intervalInCurrency.allInterval
                }
                val text = e.y.toString() + extendedInfo.signs[curCurrency] + " - " +
                        formatter.format(Date(curInterval[curInterval.size - 1 - e.x.toInt()].first * 1000))
                priceView.text = text
            }

            override fun onNothingSelected() {
                priceView.text = requireActivity().resources.getText(R.string.getPriceInTouchMomentString)
            }
        })
    }

    private fun setupCurrencyBut(){
        fun click(button: Button){
            updateData()
            listOf<Button>(
                root.findViewById<Button>(R.id.rub),
                root.findViewById<Button>(R.id.eur),
                root.findViewById<Button>(R.id.usd),
            ).forEach { it.background = requireActivity().resources.getColor(R.color.button_color).toDrawable() }
            button.background = requireActivity().resources.getColor(R.color.lightBar_color).toDrawable()
        }
        root.findViewById<Button>(R.id.rub).setOnClickListener {
            curCurrency = "rub"
            click(it as Button)
        }
        root.findViewById<Button>(R.id.eur).setOnClickListener {
            curCurrency = "eur"
            click(it as Button)
        }
        root.findViewById<Button>(R.id.usd).setOnClickListener {
            curCurrency = "usd"
            click(it as Button)
        }
    }

    private fun setupTimeBut(){
        fun click(button: Button){
            updateData()
            listOf<Button>(
                root.findViewById<Button>(R.id.monthBut),
                root.findViewById<Button>(R.id.yearBut),
                root.findViewById<Button>(R.id.allBut),
            ).forEach { it.background = requireActivity().resources.getColor(R.color.button_color).toDrawable() }
            button.background = requireActivity().resources.getColor(R.color.lightBar_color).toDrawable()
        }
        root.findViewById<Button>(R.id.monthBut).setOnClickListener {
            curTime = "month"
            click(it as Button)
        }
        root.findViewById<Button>(R.id.yearBut).setOnClickListener {
            curTime = "year"
            click(it as Button)
        }
        root.findViewById<Button>(R.id.allBut).setOnClickListener {
            curTime = "all"
            click(it as Button)
        }
    }

    private fun updateData(){
        var numDay = 0
        val intervalInCurrency = when(curCurrency){
            "rub" -> extendedInfo.rubInterval
            "eur" -> extendedInfo.euroInterval
            else -> extendedInfo.usdInterval
        }
        var curInterval: List<Double>
        var curData: BasicValues
        when(curTime){
            "month" -> {
                curInterval = intervalInCurrency.monthInterval.map { it.second }
                curData = intervalInCurrency.monthData
            }
            "year" -> {
                curInterval = intervalInCurrency.yearInterval.map { it.second }
                curData = intervalInCurrency.yearData
            }
            else -> {
                curInterval = intervalInCurrency.allInterval.map { it.second }
                curData = intervalInCurrency.allData
            }
        }

        val barEntries: ArrayList<BarEntry> = ArrayList()


        curInterval = listOf(12.3, 23.2, 43.3, 11.2, 25.0)

        for (i in curInterval.reversed())
            barEntries.add(BarEntry((numDay++).toFloat(), i.toFloat()))

        val barDataset = BarDataSet(barEntries, extendedInfo.name)
        setupBarDataSet(barDataset, requireContext())
        val barData = BarData(barDataset)

        val combinedData = CombinedData()

        combinedData.setData(barData)
        mChart.data = combinedData
        mChart.notifyDataSetChanged()
        mChart.invalidate()

        root.findViewById<TextView>(R.id.income).text =
            curData.income.toString() + extendedInfo.signs[curCurrency]
        root.findViewById<TextView>(R.id.dividends).text =
            curData.dividends.toString() + extendedInfo.signs[curCurrency]
        root.findViewById<TextView>(R.id.brokerCommission).text =
            curData.brokerCommission.toString() + extendedInfo.signs[curCurrency]
        root.findViewById<TextView>(R.id.depositsAndWithdrawalsDiffText).text =
            curData.depositsAndWithdrawalsDiff.toString() + extendedInfo.signs[curCurrency]

    }
}