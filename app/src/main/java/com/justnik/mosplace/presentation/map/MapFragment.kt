package com.justnik.mosplace.presentation.map

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.justnik.mosplace.R
import com.justnik.mosplace.data.mock.MockData
import com.justnik.mosplace.data.network.PlaceTypes
import com.justnik.mosplace.databinding.FragmentMapBinding
import com.justnik.mosplace.domain.entities.Place
import com.justnik.mosplace.helpers.observeFlow
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.ui_view.ViewProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by viewBinding()
    private val viewModel: MapViewModel by viewModels()

    private val mapView by lazy { binding.mapView }
    private val mapObjects by lazy { mapView.map.mapObjects.addCollection() }

    private val tapListener = MapObjectTapListener { mapObject, point ->
        val place = mapObject.userData as Place
        findNavController().navigate(MapFragmentDirections.actionMapFragmentToPlace(place))
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpMapView()
    }

    private fun setUpMapView() {
        mapView.map.move(
            CameraPosition(MOSCOW_LOCATION_POINT, 10f, 0f, 0f)
        )
        addPlacemarks()
    }

    private fun addPlacemarks() {
        viewModel.uiState.observeFlow(viewLifecycleOwner){ uiState ->
            uiState.places.forEach{ place ->
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
}
