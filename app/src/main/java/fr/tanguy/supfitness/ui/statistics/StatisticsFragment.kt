package fr.tanguy.supfitness.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fr.tanguy.supfitness.R
import fr.tanguy.supfitness.databinding.FragmentStatisticsBinding

class StatisticsFragment : Fragment() {

  private lateinit var statisticsViewModel: StatisticsViewModel
private var _binding: FragmentStatisticsBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    statisticsViewModel =
            ViewModelProvider(this).get(StatisticsViewModel::class.java)

    _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textDashboard
    statisticsViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })

    val view = getActivity()?.findViewById<View>(R.id.imageViewToolbar)

    if (view is ImageView) {
      val imageView = view
      imageView.setImageResource(R.drawable.statistic)
    }

    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}