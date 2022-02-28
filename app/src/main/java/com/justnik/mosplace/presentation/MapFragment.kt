package com.justnik.mosplace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.justnik.mosplace.data.mappers.Mapper
import com.justnik.mosplace.data.mock.MockData
import com.justnik.mosplace.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: MosViewModel by lazy {
        ViewModelProvider(this)[MosViewModel::class.java]
    }

    private val scope = CoroutineScope(Dispatchers.Main)

    private val mapper = Mapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext());
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpMapView()
    }

    private fun setUpMapView() {
        val mapView = binding.mapView

        mapView.map.move(
            CameraPosition(MOSCOW_LOCATION_POINT, 10f, 0f, 0f)
        )

        addPlacemarks()
    }

    private fun addPlacemarks() {
        val mapObjects = binding.mapView.map.mapObjects

        scope.launch {
            val placePoints = viewModel.loadAllPlaces()

            placePoints.forEach {
                mapObjects.addPlacemark(mapper.mapPlaceToPoint(it))
            }

            MockData().getMockPoints().forEach {
                mapObjects.addPlacemark(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }

        private val MOSCOW_LOCATION_POINT = Point(55.753789, 37.6209230)
    }
}