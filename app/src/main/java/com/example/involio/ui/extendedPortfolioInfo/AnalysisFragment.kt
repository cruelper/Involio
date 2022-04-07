package com.example.involio.ui.extendedPortfolioInfo

import android.R.attr
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import classes.network.dto.ExtendedPortfolioInfoDto
import classes.network.utils.setupPieChart
import classes.network.utils.setupPieDataset
import com.example.involio.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData

import com.github.mikephil.charting.data.PieDataSet

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

import com.github.mikephil.charting.utils.ColorTemplate
import android.text.Spannable
import android.text.SpannableString

import android.text.style.ForegroundColorSpan

import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.widget.ListView
import android.widget.TextView
import classes.PiecesAdapter
import android.view.MotionEvent
import android.widget.Toast
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener

import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import android.R.attr.x
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import kotlin.properties.Delegates


class AnalysisFragment : Fragment() {
    private lateinit var root: View
    private lateinit var extendedInfo: ExtendedPortfolioInfoDto
    private lateinit var chart: PieChart
    private lateinit var listView: ListView
    private var pieEntries: ArrayList<PieEntry> = ArrayList()
    private var indexOfLastSelectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        extendedInfo = (requireActivity() as ExtendedInfoActivity).extendedInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_analysis, container, false)
        setupChart()
        setupButtons()

        return root
    }

    private fun setupButtons(){
        val listOfButtons = listOf<Button>(
            root.findViewById<Button>(R.id.assets),
            root.findViewById<Button>(R.id.currency),
            root.findViewById<Button>(R.id.sectors),
            root.findViewById<Button>(R.id.companies),
        )

        listOfButtons.forEach { button ->
            button.setOnClickListener {
                listOfButtons.forEach { it.background = requireActivity().resources.getColor(R.color.button_color).toDrawable() }
                button.background = requireActivity().resources.getColor(R.color.lightBar_color).toDrawable()
                setupDataOnChart(button)
                setupListView(button)
            }
        }

        listOfButtons[0].callOnClick()
    }

    private fun setupDataOnChart(button: Button){
        val str2 = when (button.id){
            R.id.assets -> {
                pieEntries.clear()
                pieEntries.add(PieEntry(20f, "Гречка"))
                pieEntries.add(PieEntry(20f, "Макароны"))
                pieEntries.add(PieEntry(10f, "Рис"))
                pieEntries.add(PieEntry(10f, "Тушенка"))
                pieEntries.add(PieEntry(50f, "Пшено"))
                pieEntries.add(PieEntry(35f, "Вода"))
//                "типов активов: " + extendedInfo.assets.size.toString()
                "типов активов: 6"
            }
            R.id.currency -> {
                pieEntries.clear()
                pieEntries.add(PieEntry(0.09f, "Electricity and Gas"))
                pieEntries.add(PieEntry(0.25f, "Housing"))
                pieEntries.add(PieEntry(0.05f, "wedwedwedw"))
                "валют: " + extendedInfo.assets.size.toString()
            }
            R.id.sectors -> {
                pieEntries.clear()
                pieEntries.add(PieEntry(0.05f, "wedwedwedwerfg4r"))
                pieEntries.add(PieEntry(0.05f, "wedwedwedwerf"))
                pieEntries.add(PieEntry(0.05f, "wedwedwg5tederf4rw"))
                pieEntries.add(PieEntry(0.05f, "wedwedweerfferdw"))
                "отраслей: " + extendedInfo.assets.size.toString()
            }
            R.id.companies -> {
                pieEntries.clear()
                pieEntries.add(PieEntry(0.05f, "wedwedwedwf4rg"))
                "компаний:" + extendedInfo.assets.size.toString()
            }
            else -> ""
        }
        val str1 = extendedInfo.name + "\n"
        val sb = SpannableStringBuilder(str1 + str2)
        sb.setSpan(StyleSpan(Typeface.BOLD), 0, str1.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sb.setSpan(StyleSpan(Typeface.ITALIC), str1.length, str1.length + str2.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        chart.centerText = sb


        chart.highlightValues(null)

        val dataSet = PieDataSet(pieEntries, "Expense Category")
        setupPieDataset(dataSet, requireContext())

        val data = PieData(dataSet)

        chart.data = data
        chart.invalidate()
    }


    private fun setupChart(){
        chart = root.findViewById<PieChart>(R.id.chart)
        setupPieChart(chart, requireContext())
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                var counter = 0
                for (i in chart.legend.entries){
                    if (i.label == (e as PieEntry).label) {
//                        listView.smoothScrollToPosition(counter)

                        listView.post(Runnable { listView.smoothScrollToPosition(counter)})
                        listView.post(Runnable {
                            listView.forEach {
                                if ((it as ConstraintLayout).findViewById<TextView>(R.id.name).text.toString() == i.label)
                                    it.background = requireActivity().resources.getColor(R.color.darkBar_color).toDrawable()
                                else  it.background = requireActivity().resources.getColor(R.color.background_color).toDrawable()
                            }
                        })


                            break
                    }
                    counter+=1
                }
            }

            override fun onNothingSelected() {}
        })
    }

    private fun setupListView(button: Button){
        listView = root.findViewById<ListView>(R.id.pieces)
        val adapter = PiecesAdapter(
            requireActivity(),
            pieEntries.map { it.label.toString() }.toTypedArray(),
            pieEntries.map { it.value.toString() }.toTypedArray(),
            chart.legend.entries.map { it.formColor }.toTypedArray()
        )

        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, position, id ->
            chart.highlightValues(null)
            val h: Highlight =
                Highlight(position.toFloat(), 0, 0)
            chart.highlightValues(arrayOf(h))

            listView.post(Runnable { listView.smoothScrollToPosition(position)})
            listView.post(Runnable {
                listView.forEach {
                    if (it == view)
                        it.background = requireActivity().resources.getColor(R.color.darkBar_color).toDrawable()
                    else  it.background = requireActivity().resources.getColor(R.color.background_color).toDrawable()
                }
            })
        }
    }
}