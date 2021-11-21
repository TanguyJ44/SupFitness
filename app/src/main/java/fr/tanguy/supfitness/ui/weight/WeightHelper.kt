package fr.tanguy.supfitness.ui.weight

import fr.tanguy.supfitness.database.WeightDao

object WeightHelper {

    private var weights = mutableListOf<fr.tanguy.supfitness.database.Weight>()

    fun initWeight(dbWeight: List<fr.tanguy.supfitness.database.Weight>) {
        weights = dbWeight.toMutableList()
    }

    fun getAllWeights() = weights

    fun addItem(weightDao:WeightDao, newWeight: fr.tanguy.supfitness.database.Weight) {
        weightDao.insertWeight(newWeight)
        weights.add(newWeight)

        //initWeight(weightDao.getAll())
    }

    fun removeItem(weightDao:WeightDao, position: Int) {
        weightDao.deleteWeight(weights[position])
        weights.removeAt(position)
    }
}