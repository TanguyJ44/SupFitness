package fr.tanguy.supfitness.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import fr.tanguy.supfitness.R

import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView

import fr.tanguy.supfitness.databinding.FragmentStatisticsBinding
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import fr.tanguy.supfitness.ui.weight.WeightHelper


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

        val mSeries: LineGraphSeries<DataPoint?> = LineGraphSeries()

        val graph: GraphView = requireView().findViewById(R.id.graph) as GraphView

        val dataList = Array(WeightHelper.getSize()){ DataPoint(0.0, 0.0) }

        var index = 0
        for (weight in WeightHelper.getAllWeights()) {
            dataList[index] = DataPoint(weight.date, weight.weight!!)
            index += 1
        }

        val series = LineGraphSeries(dataList)

        series.isDrawDataPoints = true
        series.dataPointsRadius = 10F
        series.isDrawBackground = true

        graph.addSeries(series)

        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (isValueX) {
                    super.formatLabel(value, isValueX)
                } else {
                    super.formatLabel(value, isValueX) + " Kg"
                }
            }
        }

        graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity)
        graph.gridLabelRenderer.numHorizontalLabels = 3

        if (WeightHelper.getSize() > 0) {
            graph.viewport.setMinX(WeightHelper.getMinDate()!!.time.toDouble())
            graph.viewport.setMaxX(WeightHelper.getMaxDate()!!.time.toDouble())

            graph.viewport.setMinY(WeightHelper.getMinWeight()!!)
            graph.viewport.setMaxY(WeightHelper.getMaxWeight()!!)
        }

        graph.gridLabelRenderer.isHumanRounding = false

        graph.addSeries(mSeries)
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true

        graph.legendRenderer.isVisible = false

        // Zooming and scrolling
        //graph.viewport.isScalable = true; // enables horizontal zooming and scrolling
        //graph.viewport.setScalableY(true); // enables vertical zooming and scrolling
        graph.viewport.isScrollable = true
        graph.viewport.setScrollableY(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}