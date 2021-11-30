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
import fr.tanguy.supfitness.utils.SpacingItemRecyclerView

import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.tanguy.supfitness.utils.SwipeToDelete

import android.view.Gravity

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.widget.*
import fr.tanguy.supfitness.MainActivity
import fr.tanguy.supfitness.database.AppDatabase
import fr.tanguy.supfitness.databinding.FragmentWeightBinding
import java.util.*
import kotlin.math.roundToInt
import android.content.Context.MODE_PRIVATE

import android.content.SharedPreferences





class WeightFragment : Fragment() {

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

        val db = (activity as MainActivity).db
        val weightDao = (db as AppDatabase).weightDao()

        WeightHelper.initWeight(weightDao.getAll())

        val allWeight = WeightHelper.getAllWeights()
        val adapter = WeightAdapter(allWeight)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.weightList)
        val floatingActionButton: FloatingActionButton =
            requireView().findViewById(R.id.addWeightButton)
        val itemDecoration = SpacingItemRecyclerView(30, 50)
        val weightEmptyItems: TextView = requireView().findViewById(R.id.weightEmptyItems)
        val swipeToDeleteCallback =
            activity?.let { SwipeToDelete(it, adapter, weightDao, weightEmptyItems) }
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
        recyclerView.adapter = WeightAdapter(allWeight)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        itemTouchHelper?.attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter

        val actView = activity?.findViewById<View>(R.id.imageViewToolbar)

        if (actView is ImageView) {
            actView.setImageResource(R.drawable.scale)
        }

        if (weightDao.getAll().isEmpty()) {
            weightEmptyItems.visibility = TextView.VISIBLE
        } else {
            weightEmptyItems.visibility = TextView.GONE
        }

        val addWeightButton: FloatingActionButton = requireView().findViewById(R.id.addWeightButton)
        addWeightButton.setOnClickListener {

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

            val popupWeightEdit: EditText = popupView.findViewById(R.id.popupWeightEdit)
            if (WeightHelper.getSize() > 0) {
                popupWeightEdit.setText(WeightHelper.getLastWeight().toString())
            }

            val popupWeightDatePicker: DatePicker =
                popupView.findViewById(R.id.popupWeightDatePicker)
            var popupWeightValue = popupWeightEdit.text.toString().toDouble()

            // LEFT DOUBLE
            val popupWeightLeftDoubleButton: Button =
                popupView.findViewById(R.id.popupWeightLeftDoubleButton)
            popupWeightLeftDoubleButton.setOnClickListener {
                popupWeightValue = popupWeightEdit.text.toString().toDouble()
                if (popupWeightValue - 1 >= 0.0) {
                    popupWeightValue -= 1
                    popupWeightValue = (popupWeightValue * 100).roundToInt().toDouble() / 100
                    popupWeightEdit.setText(popupWeightValue.toString())
                }
            }

            // LEFT SIMPLE
            val popupWeightLeftSimpleButton: Button =
                popupView.findViewById(R.id.popupWeightLeftSimpleButton)
            popupWeightLeftSimpleButton.setOnClickListener {
                popupWeightValue = popupWeightEdit.text.toString().toDouble()
                if (popupWeightValue - 0.1 >= 0.0) {
                    popupWeightValue -= 0.1
                    popupWeightValue = (popupWeightValue * 100).roundToInt().toDouble() / 100
                    popupWeightEdit.setText(popupWeightValue.toString())
                }
            }

            // RIGHT SIMPLE
            val popupWeightRightSimpleButton: Button =
                popupView.findViewById(R.id.popupWeightRightSimpleButton)
            popupWeightRightSimpleButton.setOnClickListener {
                popupWeightValue = popupWeightEdit.text.toString().toDouble()
                popupWeightValue += 0.1
                popupWeightValue = (popupWeightValue * 100).roundToInt().toDouble() / 100
                popupWeightEdit.setText(popupWeightValue.toString())
            }

            // RIGHT DOUBLE
            val popupWeightRightDoubleButton: Button =
                popupView.findViewById(R.id.popupWeightRightDoubleButton)
            popupWeightRightDoubleButton.setOnClickListener {
                popupWeightValue = popupWeightEdit.text.toString().toDouble()
                popupWeightValue += 1
                popupWeightValue = (popupWeightValue * 100).roundToInt().toDouble() / 100
                popupWeightEdit.setText(popupWeightValue.toString())
            }

            val popupWeightValidate: Button = popupView.findViewById(R.id.popupWeightValidate)
            popupWeightValidate.setOnClickListener {

                popupWeightValue = popupWeightEdit.text.toString().toDouble()

                WeightHelper.addItem(
                    weightDao,
                    fr.tanguy.supfitness.database.Weight(
                        0,
                        popupWeightValue,
                        Date(
                            popupWeightDatePicker.year,
                            popupWeightDatePicker.month + 1,
                            popupWeightDatePicker.dayOfMonth
                        )
                    )
                )

                if (weightDao.getAll().isEmpty()) {
                    weightEmptyItems.visibility = TextView.VISIBLE
                } else {
                    weightEmptyItems.visibility = TextView.GONE
                }

                recyclerView.adapter = adapter
                popupWindow.dismiss()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}