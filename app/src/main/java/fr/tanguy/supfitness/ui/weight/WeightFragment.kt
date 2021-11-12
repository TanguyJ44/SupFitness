package fr.tanguy.supfitness.ui.weight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.tanguy.supfitness.R
import fr.tanguy.supfitness.databinding.FragmentWeightBinding

class WeightFragment : Fragment(), WeightAdapter.WeightItemListener {

    private var _binding: FragmentWeightBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeightBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allWeight = WeightObject.getAllWeights()
        val recyclerView: RecyclerView = requireView().findViewById(R.id.weightList)
        recyclerView.adapter = WeightAdapter(allWeight, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWeightItemClick(weight: Weight) {
        return
    }
}