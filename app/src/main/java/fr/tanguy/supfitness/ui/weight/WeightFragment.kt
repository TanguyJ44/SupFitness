package fr.tanguy.supfitness.ui.weight

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.tanguy.supfitness.R
import fr.tanguy.supfitness.ui.utils.SpacingItemRecyclerView

import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.tanguy.supfitness.ui.utils.SwipeToDelete

import android.view.Gravity

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.widget.*
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

    @SuppressLint("InflateParams", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allWeight = WeightHelper.getAllWeights()
        val adapter = WeightAdapter(allWeight, this)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.weightList)
        val floatingActionButton: FloatingActionButton = requireView().findViewById(R.id.addWeightButton)
        val itemDecoration = SpacingItemRecyclerView(30, 50)
        val swipeToDeleteCallback = activity?.let { SwipeToDelete(it, adapter) }
        val itemTouchHelper = swipeToDeleteCallback?.let { ItemTouchHelper(it) }

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
        itemTouchHelper?.attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter

        val view = activity?.findViewById<View>(R.id.imageViewToolbar)

        if (view is ImageView) {
            val imageView = view
            imageView.setImageResource(R.drawable.scale)
        }

        val addWeightButton: FloatingActionButton = requireView().findViewById(R.id.addWeightButton)
        addWeightButton.setOnClickListener(View.OnClickListener {

            val inflater = activity?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
            val popupView: View = inflater!!.inflate(R.layout.weight_add_popup, null)

            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true

            val popupWindow = PopupWindow(popupView, width, height, focusable)

            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            val buttonPopupClose: Button = popupView.findViewById(R.id.popupWeightCloseButton)
            buttonPopupClose.setOnClickListener {
                popupWindow.dismiss()
            }

            /*val buttonAddWeight: Button = popupView.findViewById(R.id.buttonAddWeight)
            buttonAddWeight.setOnClickListener {

                WeightHelper.addItem(10.2, Date())
                recyclerView.adapter = adapter

            }*/

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWeightItemClick(weight: Weight) {
        return
    }

}