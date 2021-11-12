package fr.tanguy.supfitness.ui.weight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.tanguy.supfitness.R
import java.util.*

class WeightAdapter(private val weightList: List<Weight>, private val itemListener: WeightItemListener) :
    RecyclerView.Adapter<WeightAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.weight_item, parent, false)

        return ExampleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, index: Int) {
        val currentItem = weightList[index]

        holder.weightListWeight.text = currentItem.weight
        holder.weightListDate.text = currentItem.date
        holder.itemView.setOnClickListener{
            itemListener.onWeightItemClick(currentItem)
        }
    }

    override fun getItemCount() = weightList.size

    class ExampleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weightListWeight: TextView = itemView.findViewById(R.id.weightListWeight)
        val weightListDate: TextView = itemView.findViewById(R.id.weightListDate)
    }

    /* interface CocktailsItemListener{
        fun onCocktailsItemClick(cocktail: Weight)
    } */
}

data class Weight(
    val weight: Double,
    val date: Date,
)