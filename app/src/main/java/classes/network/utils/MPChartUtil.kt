package classes.network.utils

import android.content.Context
import com.example.involio.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet

fun setupLineChart(chart: LineChart, context: Context){
    val legend = chart.legend
    legend.isEnabled = false
    chart.axisLeft.setDrawGridLines(false)
    chart.axisLeft.setDrawLabels(false)
    chart.axisLeft.textColor = (context.resources.getColor(R.color.text_color))
    chart.xAxis.setDrawGridLines(false)
    chart.xAxis.setDrawLabels(false)
    chart.description.isEnabled = false
    chart.axisRight.setDrawLabels(false)
    chart.axisRight.setDrawGridLines(false)
    chart.setPadding(0, 0, 0, 0)

    chart.setScaleEnabled(false)

    chart.isDragEnabled = true

    // Чтобы без границ
    chart.setDrawGridBackground(false)
    chart.xAxis.setDrawGridLines(false)
    chart.xAxis.setDrawAxisLine(false)
    chart.axisLeft.setDrawGridLines(false)
    chart.axisLeft.setDrawAxisLine(false)
    chart.axisRight.setDrawGridLines(false)
    chart.axisRight.setDrawAxisLine(false)

    chart.setViewPortOffsets(0f, 0f, 0f, 0f)


    // График будет анимироваться 1 секунды
    chart.animateY(1000)

    chart.isHighlightPerDragEnabled = true
    chart.isHighlightPerTapEnabled = true
}

fun setupLineDataSet(dataset: LineDataSet, context: Context){
    dataset.setDrawCircles(false)
    dataset.setDrawValues(false)
    dataset.valueTextColor = (context.resources.getColor(R.color.text_color))
    // Заполнение под графиком
    dataset.setDrawFilled(true)
    dataset.fillColor = (context.resources.getColor(R.color.lightBar_color))
    dataset.lineWidth = dataset.lineWidth * 1.5f
    dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
    // Изменение цвета
    dataset.color = (context.resources.getColor(R.color.lightBar_color))
    dataset.highLightColor = (context.resources.getColor(R.color.text_color))
    dataset.highlightLineWidth = dataset.highlightLineWidth * 1.5f
}