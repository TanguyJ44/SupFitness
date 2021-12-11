package fr.tanguy.supfitness

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import fr.tanguy.supfitness.databinding.ActivityTrainingBinding
import android.widget.Toast

/*
*
* Attention : certaines parties du code proviennent directement de librairie disponible
* sur internet. Certaines méthodes ci-dessous ne sont pas de moi. Je n'aime pas réinventer la roue,
* cette invention fonctionne déjà à merveille :)
*
* */
class TrainingActivity : AppCompatActivity(), SensorEventListener, OnMapReadyCallback {

    private lateinit var binding: ActivityTrainingBinding

    private var onPause = false

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var polylineOptions = PolylineOptions()
    private val locationCallback = object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)
            locationResult ?: return

            addLocationToRoute(locationResult.locations)

            locationResult.locations.forEach {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15F))
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_FINE_LOCATION = 1
        private const val REQUEST_CODE_ACTIVITY_RECOGNITION = 2
    }

    private var isTracking: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrainingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val pauseButton: FloatingActionButton = findViewById(R.id.pauseButton)
        pauseButton.setOnClickListener {
            Toast.makeText(this, "Maintenez le bouton pause appuyé pour arrêter / reprendre l'entrainement",
                Toast.LENGTH_LONG).show()
        }
        pauseButton.setOnLongClickListener {
            if (!onPause) {
                onPause = true
                stopTracking(false)
            } else {
                onPause = false
                startTracking()
            }
            true
        }

        val stopButton: FloatingActionButton = findViewById(R.id.stopButton)
        stopButton.setOnClickListener {
            Toast.makeText(this, "Maintenez le bouton stop appuyé pour arrêter l'entrainement",
                Toast.LENGTH_LONG).show()
        }
        stopButton.setOnLongClickListener {
            stopTracking(true)
            true
        }
    }

    @SuppressLint("InlinedApi")
    @AfterPermissionGranted(REQUEST_CODE_ACTIVITY_RECOGNITION)
    private fun startTracking() {

        val isActivityRecognitionPermissionFree = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        val isActivityRecognitionPermissionGranted = EasyPermissions.hasPermissions(this,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
        Log.d("TAG", "Is ACTIVITY_RECOGNITION permission granted $isActivityRecognitionPermissionGranted")
        if (isActivityRecognitionPermissionFree || isActivityRecognitionPermissionGranted) {
            setupStepCounterListener()
            setupLocationChangeListener()
        } else {
            EasyPermissions.requestPermissions(
                host = this,
                rationale = "For showing your step counts and calculate the average pace.",
                requestCode = REQUEST_CODE_ACTIVITY_RECOGNITION,
                perms = arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
            )
        }
    }

    private fun stopTracking(finish: Boolean) {
        polylineOptions = PolylineOptions()

        fusedLocationProviderClient.removeLocationUpdates(locationCallback)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.unregisterListener(this, stepCounterSensor)

        if (finish) {
            finish()
        }
    }

    private fun setupStepCounterListener() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor ?: return
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    @AfterPermissionGranted(REQUEST_CODE_FINE_LOCATION)
    private fun setupLocationChangeListener() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000 // 5000ms (5s)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    @AfterPermissionGranted(REQUEST_CODE_FINE_LOCATION)
    private fun showUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true

    }

    fun addLocationToRoute(locations: List<Location>) {
        mMap.clear()
        val originalLatLngList = polylineOptions.points
        val latLngList = locations.map {
            LatLng(it.latitude, it.longitude)
        }
        originalLatLngList.addAll(latLngList)
        mMap.addPolyline(polylineOptions)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("TAG", "onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        Log.d("TAG", "onSensorChanged")
        sensorEvent ?: return
        val firstSensorEvent = sensorEvent.values.firstOrNull() ?: return
        Log.d("TAG", "Steps count: $firstSensorEvent ")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        showUserLocation()

        mMap.clear()
        isTracking = true

        startTracking()
    }

}