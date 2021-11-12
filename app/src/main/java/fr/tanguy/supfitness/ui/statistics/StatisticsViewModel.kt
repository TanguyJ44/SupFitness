package fr.tanguy.supfitness.ui.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StatisticsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Page SUIVI DU POIDS"
    }
    val text: LiveData<String> = _text
}