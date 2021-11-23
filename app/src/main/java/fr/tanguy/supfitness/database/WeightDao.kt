package fr.tanguy.supfitness.database

import androidx.room.*

@Dao
interface WeightDao {

    @Query("SELECT * FROM weight")
    fun getAll(): List<Weight>

    @Query("SELECT * FROM weight WHERE id IN (:weightId)")
    fun getAllById(weightId: IntArray): List<Weight>

    @Insert
    fun insertWeight(weight: Weight): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weight: Weight)

    @Delete
    fun deleteWeight(weight: Weight)

}