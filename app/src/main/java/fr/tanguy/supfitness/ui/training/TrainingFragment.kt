package fr.tanguy.supfitness.ui.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.tanguy.supfitness.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

  private lateinit var trainingViewModel: TrainingViewModel
private var _binding: FragmentTrainingBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    trainingViewModel =
      ViewModelProvider(this).get(TrainingViewModel::class.java)

    _binding = FragmentTrainingBinding.inflate(inflater, container, false)

    return binding.root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}