package fr.tanguy.supfitness.ui.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TrainingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Page ENTRAINEMENT"
    }
    val text: LiveData<String> = _text
}