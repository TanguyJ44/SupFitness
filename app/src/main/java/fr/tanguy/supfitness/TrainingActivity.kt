package fr.tanguy.supfitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.tanguy.supfitness.databinding.ActivityTrainingBinding


class TrainingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}