package com.justnik.mosplace.presentation.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.justnik.mosplace.R
import com.justnik.mosplace.data.network.PlaceTypes
import com.justnik.mosplace.databinding.FragmentMapBinding
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.helpers.hideSupportActionBar
import com.justnik.mosplace.helpers.observeFlow
import com.justnik.mosplace.helpers.showSupportActionBar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.ui_view.ViewProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), UserLocationObjectListener {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding!!

    private val viewModel: MapViewModel by viewModels()

    private val locationPermissionRequestLauncher = registerForActivityResult(
        RequestMultiplePermissions(),
        this::onGotPermissionsLocationResult
    )

    private lateinit var mapView: MapView
    private lateinit var mapObjects: MapObjectCollection

    private lateinit var userLocationLayer: UserLocationLayer

    private val tapListener = MapObjectTapListener { mapObject, point ->
        val place = mapObject.userData as Place
        findNavController().navigate(MapFragmentDirections.actionMapFragmentToPlace(place))
        true
    }

    private val locationManager by lazy {
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        mapView = binding.mapView
        mapObjects = mapView.map.mapObjects.addCollection()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpMapView()
        checkLocationPermissions()

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        hideSupportActionBar()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        showSupportActionBar()
        _binding = null
    }

    private fun checkLocationPermissions() {
        val coarseLocationPermission = android.Manifest.permission.ACCESS_COARSE_LOCATION
        val fineLocationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION

        locationPermissionRequestLauncher.launch(
            arrayOf(coarseLocationPermission, fineLocationPermission)
        )
    }

    private fun onGotPermissionsLocationResult(grantResult: Map<String, Boolean>) {
        if (grantResult.entries.all { it.value }) {
            setupUserLocationLayer()
            binding.tvPermissionError.visibility = View.GONE
        }
        else {
            binding.tvPermissionError.visibility = View.VISIBLE
        }
    }

    private fun setUpMapView() {
        mapView.map.move(
            CameraPosition(MOSCOW_LOCATION_POINT, 10f, 0f, 0f)
        )
        addPlacemarks()
    }

    @SuppressLint("MissingPermission")
    private fun setupUserLocationLayer() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)

        binding.fabCurrentLocation.setOnClickListener {
            val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (hasGps) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null){
                    val point = Point(location.latitude, location.longitude)
                    mapView.map.move(
                        CameraPosition(point, 14.5f, 0f, 0f),
                        Animation(Animation.Type.SMOOTH, 0.8f),
                        null
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.gps_turned_off),
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    private fun addPlacemarks() {
        viewModel.uiState.observeFlow(viewLifecycleOwner) { uiState ->
            uiState.places.forEach { place ->
                val point = Point(place.latitude, place.longitude)
                val viewProvider = ViewProvider(getImageViewByPlaceType(place.type))
                val mark = mapObjects.addPlacemark(point, viewProvider)
                mark.userData = place
                mark.addTapListener(tapListener)
            }
        }
    }

    private fun getImageViewByPlaceType(type: String): ImageView {
        val drawableId = when (type) {
            PlaceTypes.PARK -> R.drawable.ic_park
            PlaceTypes.RESTAURANT -> R.drawable.ic_restaurant
            else -> R.drawable.ic_photo_camera
        }
        val imageView = ImageView(requireContext())
        imageView.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                drawableId
            )
        )
        return imageView
    }

    companion object {
        private val MOSCOW_LOCATION_POINT = Point(55.753789, 37.6209230)
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }
}
