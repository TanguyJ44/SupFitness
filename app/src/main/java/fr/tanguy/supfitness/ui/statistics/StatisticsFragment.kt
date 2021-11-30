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
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import fr.tanguy.supfitness.ui.weight.WeightHelper
import java.lang.Number
import java.text.NumberFormat
import java.util.*


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

        statCalcTotal()

        val mSeries: LineGraphSeries<DataPoint?> = LineGraphSeries()

        val graph: GraphView = requireView().findViewById(R.id.graph) as GraphView

        val dataList = Array(WeightHelper.getSize()){ DataPoint(0.0, 0.0) }

        var calendar = Calendar.getInstance()
        var date = calendar.time

        var index = 0
        for (weight in WeightHelper.getAllWeights()) {
            calendar.set(weight.date!!.year, weight.date!!.month, weight.date!!.date)
            date = calendar.time

            //dataList[index] = DataPoint(weight.date!!.time.toDouble(), weight.weight!!)
            dataList[index] = DataPoint(date, weight.weight!!)

            //println("LOOP :: ${weight.date.time.toDouble()}")
            println("LOOP :: ${date.time.toDouble()}")

            index += 1
        }

        val series = LineGraphSeries(dataList)

        series.isDrawDataPoints = true
        series.dataPointsRadius = 10F
        series.isDrawBackground = true

        graph.addSeries(series)

        /*graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    super.formatLabel(value, isValueX)
                } else {
                    super.formatLabel(value, isValueX) + " Kg"
                }
            }
        }*/

        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
        graph.gridLabelRenderer.numHorizontalLabels = 3

        if (WeightHelper.getSize() > 0) {
            //graph.viewport.setMinX(WeightHelper.getMinDate()!!.time.toDouble())
            //graph.viewport.setMaxX(WeightHelper.getMaxDate()!!.time.toDouble())

            calendar.set(WeightHelper.getMinDate()!!.year, WeightHelper.getMinDate()!!.month, WeightHelper.getMinDate()!!.date)
            date = calendar.time
            graph.viewport.setMinX(date.time.toDouble())

            println("MIN-X :: ${date.time.toDouble()}")

            calendar.set(WeightHelper.getMaxDate()!!.year, WeightHelper.getMaxDate()!!.month, WeightHelper.getMaxDate()!!.date)
            date = calendar.time
            graph.viewport.setMaxX(date.time.toDouble())

            println("MAX-X :: ${date.time.toDouble()}")


            //println("MIN-X :: ${WeightHelper.getMinDate()!!.time.toDouble()}")
            //println("MAX-X :: ${WeightHelper.getMaxDate()!!.time.toDouble()}")

            graph.viewport.setMinY(WeightHelper.getMinWeight()!!)
            graph.viewport.setMaxY(WeightHelper.getMaxWeight()!!)
        }

        graph.gridLabelRenderer.isHumanRounding = false

        graph.addSeries(mSeries)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true

        graph.legendRenderer.isVisible = false

        // Zooming and scrolling
        graph.viewport.isScalable = true; // enables horizontal zooming and scrolling
        graph.viewport.setScalableY(true); // enables vertical zooming and scrolling
        //graph.viewport.isScrollable = true
        //graph.viewport.setScrollableY(true)
    }

    private fun statCalcTotal() {
        val titleCard4: TextView = requireView().findViewById(R.id.titleCard4)
        var averageList: MutableList<Double> = mutableListOf()

        for (weight in WeightHelper.getAllWeights()) {
            averageList.add(weight.weight!!)
        }

        val format: NumberFormat = NumberFormat.getInstance()
        format.minimumFractionDigits = 1

        titleCard4.text = "${format.format(averageList.average())} Kg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}