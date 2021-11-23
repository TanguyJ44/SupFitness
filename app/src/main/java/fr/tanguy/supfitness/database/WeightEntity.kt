package fr.tanguy.supfitness.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Weight(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "weight") val weight: Double?,
    @ColumnInfo(name = "date") val date: Date?
)