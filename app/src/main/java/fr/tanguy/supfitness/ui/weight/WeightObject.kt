package fr.tanguy.supfitness.ui.weight

import java.util.*

object WeightObject {

    private val weights = mutableListOf(
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date()),
        Weight(59.2, Date())
    )

    fun getAllWeights() = weights

}

data class Weight(
    val weight: Double,
    val date: Date,
)