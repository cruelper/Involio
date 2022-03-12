package com.example.involio.ui.tabbs

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.involio.R
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.XAxis
import android.widget.TextView
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginLeft
import classes.network.dto.StockInfoDto
import classes.network.utils.setupLineChart
import classes.network.utils.setupLineDataSet
import com.example.involio.StockContentActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData

import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class SecondTabFragment : Fragment() {

    private lateinit var root: View
    private lateinit var chart: LineChart
    private lateinit var stockInfo: StockInfoDto
    private var curTimeChart: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.tab_second_fragment, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stockInfo = (activity as StockContentActivity).stockInfo

        root.findViewById<TextView>(R.id.curPrice).text = String.format("%.3f", stockInfo.currentPrice)

        setButListeners()

        chart = root.findViewById(R.id.chart)
        setupLineChart(chart, requireContext())

        // Для показа данных, при косании по графику
        val priceView = root.findViewById<TextView>(R.id.priceInfoInTouchMoment)
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry, h: Highlight) {
                val formatter =  when(curTimeChart){
                    "day" -> SimpleDateFormat("yyyy-MM-dd HH:mm")
                    "month" -> SimpleDateFormat("yyyy-MM-dd")
                    "year" -> SimpleDateFormat("yyyy-MM-dd")
                    else -> SimpleDateFormat("yyyy-MM")
                }
                val curInterval = when(curTimeChart){
                    "day" -> stockInfo.dayInterval
                    "month" -> stockInfo.monthInterval
                    "year" -> stockInfo.yearInterval
                    else -> stockInfo.fullInterval
                }
                val text = e.y.toString() + stockInfo.currencySign + " - " + formatter.format(Date(curInterval[e.x.toInt()].first * 1000))
                priceView.text = text
            }

            override fun onNothingSelected() {
                priceView.text = requireActivity().resources.getText(R.string.getPriceInTouchMomentString)
            }
        })

        root.findViewById<Button>(R.id.dayBut).callOnClick()
    }

    private fun setButListeners(){
        val buttons = listOf<Button>(
            root.findViewById(R.id.dayBut),
            root.findViewById(R.id.monthBut),
            root.findViewById(R.id.yearBut),
            root.findViewById(R.id.allBut)
        )
        buttons.forEach{ but ->
            but.setOnClickListener { it ->
                val timeChart = when(it.id){
                    R.id.dayBut -> "day"
                    R.id.monthBut -> "month"
                    R.id.yearBut -> "year"
                    else -> "all"
                }
                if (timeChart != curTimeChart){
                    curTimeChart = timeChart
                    buttons.forEach{ curBut -> curBut.background = requireActivity().resources.getColor(R.color.button_color).toDrawable()}
                    it.background = requireActivity().resources.getColor(R.color.lightBar_color).toDrawable()
                    updateChart()
                }
            }
        }
    }

    private fun updateChart(){
        val entries: ArrayList<Entry> = ArrayList()
        var numDay = 0
        val curInterval = when(curTimeChart){
            "day" -> stockInfo.dayInterval
            "month" -> stockInfo.monthInterval
            "year" -> stockInfo.yearInterval
            else -> stockInfo.fullInterval
        }
        for (i in curInterval)
            entries.add(Entry((numDay++).toFloat(), i.second.toFloat()))

        val dataset = LineDataSet(entries, stockInfo.nameCompany)
        setupLineDataSet(dataset, requireContext())
        val data = LineData(dataset)
        chart.data = data
        chart.notifyDataSetChanged()
        chart.invalidate()
    }
}
