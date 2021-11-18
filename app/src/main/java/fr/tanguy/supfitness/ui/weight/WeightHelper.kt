package fr.tanguy.supfitness.ui.weight

import java.util.*

object WeightHelper {

    //private val weights = mutableListOf<Weight>()

    private val weights = mutableListOf(
            Weight(59.2, Date()),
            Weight(59.6, Date()),
            Weight(59.4, Date()),
            Weight(59.0, Date()),
            Weight(59.8, Date()),
            Weight(59.9, Date()),
            Weight(59.1, Date()),
            Weight(59.3, Date()),
            Weight(59.1, Date()),
            Weight(59.5, Date()),
            Weight(59.7, Date()),
            Weight(59.8, Date()),
            Weight(59.7, Date()),
            Weight(59.2, Date()),
            Weight(59.1, Date()),
            Weight(59.2, Date()),
            Weight(59.4, Date()),
            Weight(59.5, Date()),
            Weight(59.2, Date()),
            Weight(59.8, Date())
    )

    fun getAllWeights() = weights
    fun removeItem(position: Int) = weights.removeAt(position)
    fun addItem(weight: Double, date: Date) = weights.add(0, Weight(weight, date))
}

data class Weight(
        val weight: Double,
        val date: Date,
)