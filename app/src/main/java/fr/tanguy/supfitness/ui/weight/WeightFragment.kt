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
import fr.tanguy.supfitness.ui.utils.SpacingItemRecyclerView

import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.tanguy.supfitness.ui.utils.SwipeToDeleteCallback


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
        val floatingActionButton: FloatingActionButton = requireView().findViewById(R.id.floatingActionButton)
        val itemDecoration = SpacingItemRecyclerView(30, 50)
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(WeightAdapter(allWeight, this)))

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && floatingActionButton.isShown) {
                    floatingActionButton.hide()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    floatingActionButton.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.adapter = WeightAdapter(allWeight, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWeightItemClick(weight: Weight) {
        return
    }
}