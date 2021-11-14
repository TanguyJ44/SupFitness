package fr.tanguy.supfitness.ui.weight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.tanguy.supfitness.R

class WeightAdapter(
    private val weightList: List<Weight>,
    private val itemListener: WeightItemListener
) :
    RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.weight_item, parent, false)

        return WeightViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, index: Int) {
        val currentItem = weightList[index]

        /*if (index == 0) {
            holder.itemView.visibility = View.GONE
        }*/

        holder.weightListWeight.text = "${currentItem.weight}"
        holder.weightListDate.text = "${currentItem.date}"
        holder.itemView.setOnClickListener {
            itemListener.onWeightItemClick(currentItem)
        }
    }

    override fun getItemCount() = weightList.size

    class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weightListWeight: TextView = itemView.findViewById(R.id.weightListWeight)
        val weightListDate: TextView = itemView.findViewById(R.id.weightListDate)
    }

    interface WeightItemListener {
        fun onWeightItemClick(weight: Weight)
    }

}