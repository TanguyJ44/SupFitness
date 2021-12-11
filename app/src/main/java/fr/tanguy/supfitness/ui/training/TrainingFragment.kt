package fr.tanguy.supfitness.ui.training

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.tanguy.supfitness.R
import fr.tanguy.supfitness.databinding.FragmentTrainingBinding

import android.os.Vibrator
import fr.tanguy.supfitness.TrainingActivity


class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTrainingBinding.inflate(inflater, container, false)

        val view = activity?.findViewById<View>(R.id.imageViewToolbar)

        if (view is ImageView) {
            view.setImageResource(R.drawable.running)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var startTimer: Int

        val countStart: TextView = requireView().findViewById(R.id.countStart)
        val startButton: FloatingActionButton = requireView().findViewById(R.id.startButton)
        val trainingListButton: Button = requireView().findViewById(R.id.trainingListButton)

        startButton.setOnClickListener {
            startButton.visibility = FloatingActionButton.GONE
            trainingListButton.visibility = Button.GONE

            startTimer = 5

            val timer = object: CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    countStart.text = "$startTimer"
                    startTimer -= 1
                }
                override fun onFinish() {
                    startButton.visibility = FloatingActionButton.VISIBLE
                    trainingListButton.visibility = Button.VISIBLE

                    val vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                    vibrator!!.vibrate(400)

                    val trainingActivityIntent = Intent(requireActivity(), TrainingActivity::class.java)
                    startActivity(trainingActivityIntent)
                }
            }
            timer.start()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}