package classes.network.utils

import android.content.Context
import android.graphics.Color
import androidx.core.graphics.drawable.toDrawable
import com.example.involio.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

fun setupChart(chart: BarLineChartBase<*>, context: Context){
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

fun setupBarDataSet(dataset: BarDataSet, context: Context){
    dataset.setDrawValues(false)
    dataset.valueTextColor = (context.resources.getColor(R.color.text_color))
    // Изменение цвета
    dataset.color = (context.resources.getColor(R.color.lightBar_color))
    dataset.highLightColor = (context.resources.getColor(R.color.text_color))
}

fun setupPieChart(chart: PieChart, context: Context){
    chart.setUsePercentValues(true)
    chart.holeRadius = 80f
    chart.setHoleColor(context.resources.getColor(R.color.background_color))
    chart.description.isEnabled = false
    chart.isRotationEnabled = true
    chart.dragDecelerationFrictionCoef = 0.9f
    chart.rotationAngle = 0f
    chart.isHighlightPerTapEnabled = true
    chart.animateY(1400, Easing.EaseInOutQuad)
    chart.setDrawEntryLabels(false)
    chart.setCenterTextColor(context.resources.getColor(R.color.text_color))
    chart.setCenterTextSize(23f)
    chart.legend.isEnabled = false
}

fun setupPieDataset(dataset: PieDataSet, context: Context){
    dataset.setDrawValues(false)
    val colors: ArrayList<Int> = ArrayList()
    colors.addAll(ColorTemplate.MATERIAL_COLORS.toTypedArray())
    colors.addAll(ColorTemplate.JOYFUL_COLORS.toTypedArray())
    colors.addAll(ColorTemplate.PASTEL_COLORS.toTypedArray())
    colors.addAll(ColorTemplate.COLORFUL_COLORS.toTypedArray())
    colors.addAll(ColorTemplate.VORDIPLOM_COLORS.toTypedArray())

    dataset.colors = colors.toSet().shuffled()
}