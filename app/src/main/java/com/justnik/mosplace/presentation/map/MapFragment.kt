package com.justnik.mosplace.presentation.map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        }
    }

    private fun setUpMapView() {
        mapView.map.move(
            CameraPosition(MOSCOW_LOCATION_POINT, 10f, 0f, 0f)
        )
        addPlacemarks()
    }

    private fun setupUserLocationLayer() {
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        userLocationLayer.isVisible = true
        userLocationLayer.isHeadingEnabled = true
        userLocationLayer.setObjectListener(this)
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
//        userLocationLayer.setAnchor(
//            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
//            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
//        )
//
//        userLocationView.arrow.setIcon(
//            ImageProvider.fromResource(
//                requireContext(), R.drawable.ic_arrow_back
//            )
//        )
//
//        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()
//
//        pinIcon.setIcon(
//            "icon",
//            ImageProvider.fromResource(requireContext(), R.drawable.ic_account),
//            IconStyle().setAnchor(PointF(0f, 0f))
//                .setRotationType(RotationType.ROTATE)
//                .setZIndex(0f)
//                .setScale(1f)
//        )
//
//        pinIcon.setIcon(
//            "pin",
//            ImageProvider.fromResource(requireContext(), R.drawable.ic_check),
//            IconStyle().setAnchor(PointF(0.5f, 0.5f))
//                .setRotationType(RotationType.ROTATE)
//                .setZIndex(1f)
//                .setScale(0.5f)
//        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }
}
