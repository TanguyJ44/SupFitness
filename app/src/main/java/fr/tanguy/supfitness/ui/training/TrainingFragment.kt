package fr.tanguy.supfitness.ui.training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import fr.tanguy.supfitness.R
import fr.tanguy.supfitness.databinding.FragmentTrainingBinding

class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTrainingBinding.inflate(inflater, container, false)

        val view = getActivity()?.findViewById<View>(R.id.imageViewToolbar)

        if (view is ImageView) {
            val imageView = view
            imageView.setImageResource(R.drawable.running)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}