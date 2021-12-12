package fr.tanguy.supfitness.ui.training

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
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
import androidx.annotation.RequiresApi
import com.vmadalin.easypermissions.EasyPermissions
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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var startTimer: Int

        val countStart: TextView = requireView().findViewById(R.id.countStart)
        val startButton: FloatingActionButton = requireView().findViewById(R.id.startButton)
        val trainingListButton: Button = requireView().findViewById(R.id.trainingListButton)

        startButton.setOnClickListener {

            val isActivityRecognitionPermissionFree = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
            val isActivityRecognitionPermissionGranted = EasyPermissions.hasPermissions(
                requireActivity(),
                Manifest.permission.ACTIVITY_RECOGNITION
            )

            if (EasyPermissions.hasPermissions(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                && (isActivityRecognitionPermissionFree || isActivityRecognitionPermissionGranted)
            ) {

                startButton.visibility = FloatingActionButton.GONE
                trainingListButton.visibility = Button.GONE

                startTimer = 5

                val timer = object : CountDownTimer(5000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        countStart.text = "$startTimer"
                        startTimer -= 1
                    }

                    override fun onFinish() {
                        startButton.visibility = FloatingActionButton.VISIBLE
                        trainingListButton.visibility = Button.VISIBLE

                        val vibrator =
                            requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                        vibrator!!.vibrate(400)

                        val trainingActivityIntent =
                            Intent(requireActivity(), TrainingActivity::class.java)
                        startActivity(trainingActivityIntent)
                    }
                }
                timer.start()

            } else {
                EasyPermissions.requestPermissions(
                    host = this,
                    rationale = "For showing your current location on the map.",
                    requestCode = 1,
                    perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                )
                EasyPermissions.requestPermissions(
                    host = this,
                    rationale = "For showing your step counts and calculate the average pace.",
                    requestCode = 2,
                    perms = arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
                )
            }


        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}