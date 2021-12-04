package fr.tanguy.supfitness.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import fr.tanguy.supfitness.R

import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView

import fr.tanguy.supfitness.databinding.FragmentStatisticsBinding
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import fr.tanguy.supfitness.ui.weight.WeightHelper
import java.text.NumberFormat
import java.util.*
import com.jjoe64.graphview.DefaultLabelFormatter
import java.text.SimpleDateFormat


class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val view = activity?.findViewById<View>(R.id.imageViewToolbar)

        if (view is ImageView) {
            view.setImageResource(R.drawable.statistic)
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statCalc7Days()
        statCalc30Days()
        statCalc90Days()
        statCalcTotal()

        val mSeries: LineGraphSeries<DataPoint?> = LineGraphSeries()

        val graph: GraphView = requireView().findViewById(R.id.graph) as GraphView

        val dataList = Array(WeightHelper.getSize()){ DataPoint(0.0, 0.0) }

        var calendar = Calendar.getInstance()
        var date = calendar.time

        var index = 0
        for (weight in WeightHelper.getAllWeights()) {
            calendar = toCalendar(weight.date)
            date = calendar.time

            dataList[index] = DataPoint(date, weight.weight!!)

            index += 1
        }

        val series = LineGraphSeries(dataList)

        series.isDrawBackground = true

        graph.addSeries(series)

        val sdf:SimpleDateFormat = SimpleDateFormat("dd/MM/yy")

        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    sdf.format(Date(value.toLong()))
                } else {
                    super.formatLabel(value, isValueX)
                }
            }
        }

        graph.gridLabelRenderer.setHorizontalLabelsAngle(1);

        if (WeightHelper.getSize() > 0) {

            calendar = toCalendar(WeightHelper.getMinDate())
            date = calendar.time
            graph.viewport.setMinX(date.time.toDouble())

            calendar = toCalendar(WeightHelper.getMaxDate())
            date = calendar.time
            graph.viewport.setMaxX(date.time.toDouble())

            graph.viewport.setMinY(WeightHelper.getMinWeight()!!)
            graph.viewport.setMaxY(WeightHelper.getMaxWeight()!!)
        }

        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true
        graph.gridLabelRenderer.isHumanRounding = false

        graph.legendRenderer.isVisible = false

        graph.viewport.isScalable = true;
        graph.viewport.setScalableY(true);
        graph.viewport.isScrollable = true
        graph.viewport.setScrollableY(true)
    }

    private fun toCalendar(date: Date?): Calendar? {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    private fun statCalc7Days() {
        val titleCard1: TextView = requireView().findViewById(R.id.titleCard1)
        val maxDate = Date()
        val minDate = Date(Date().year, Date().month, Date().date-7)
        lateinit var checkDate: Date
        var totalWeight = 0.0
        var countWeight = 0

        for (weight in WeightHelper.getAllWeights()) {
            checkDate = Date(weight.date!!.year - 1900, weight.date.month - 1, weight.date.date)

            if (checkDate.after(minDate) && checkDate.before(maxDate)) {
                totalWeight += weight.weight!!
                countWeight += 1
            }
        }

        val format: NumberFormat = NumberFormat.getInstance()
        format.minimumFractionDigits = 1

        if (countWeight == 0) {
            titleCard1.text = "-"
        } else {
            titleCard1.text = "${format.format(totalWeight / countWeight)} Kg"
        }
    }

    private fun statCalc30Days() {
        val titleCard2: TextView = requireView().findViewById(R.id.titleCard2)
        val maxDate = Date()
        val minDate = Date(Date().year, Date().month, Date().date-30)
        lateinit var checkDate: Date
        var totalWeight = 0.0
        var countWeight = 0

        for (weight in WeightHelper.getAllWeights()) {
            checkDate = Date(weight.date!!.year - 1900, weight.date.month - 1, weight.date.date)

            if (checkDate.after(minDate) && checkDate.before(maxDate)) {
                totalWeight += weight.weight!!
                countWeight += 1
            }
        }

        val format: NumberFormat = NumberFormat.getInstance()
        format.minimumFractionDigits = 1

        if (countWeight == 0) {
            titleCard2.text = "-"
        } else {
            titleCard2.text = "${format.format(totalWeight / countWeight)} Kg"
        }
    }

    private fun statCalc90Days() {
        val titleCard3: TextView = requireView().findViewById(R.id.titleCard3)
        val maxDate = Date()
        val minDate = Date(Date().year, Date().month, Date().date-90)
        lateinit var checkDate: Date
        var totalWeight = 0.0
        var countWeight = 0

        for (weight in WeightHelper.getAllWeights()) {
            checkDate = Date(weight.date!!.year - 1900, weight.date.month - 1, weight.date.date)

            if (checkDate.after(minDate) && checkDate.before(maxDate)) {
                totalWeight += weight.weight!!
                countWeight += 1
            }
        }

        val format: NumberFormat = NumberFormat.getInstance()
        format.minimumFractionDigits = 1

        if (countWeight == 0) {
            titleCard3.text = "-"
        } else {
            titleCard3.text = "${format.format(totalWeight / countWeight)} Kg"
        }
    }

    private fun statCalcTotal() {
        val titleCard4: TextView = requireView().findViewById(R.id.titleCard4)
        var averageList: MutableList<Double> = mutableListOf()
        var countWeight = 0

        for (weight in WeightHelper.getAllWeights()) {
            averageList.add(weight.weight!!)
            countWeight += 1
        }

        val format: NumberFormat = NumberFormat.getInstance()
        format.minimumFractionDigits = 1

        if (countWeight > 0) {
            titleCard4.text = "${format.format(averageList.average())} Kg"
        } else {
            titleCard4.text = "-"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}