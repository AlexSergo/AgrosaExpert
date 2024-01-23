package ru.argosaexpert.presentation.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import ru.argosaexpert.databinding.FragmentMapBinding
import ru.argosaexpert.presentation.map.MapTools.getOSMMapTile


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(layoutInflater)

        setupOsmdroidMap()
        return binding.root
    }

    private fun setupOsmdroidMap() = binding.run {
        mapView.setUseDataConnection(true)
        mapView.tileProvider = MapTileProviderBasic(context)
        mapView.setTileSource(getOSMMapTile())
        mapView.controller.setZoom(4.0)

        mapView.setMultiTouchControls(true)
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        val rotationGestureOverlay = RotationGestureOverlay(mapView)
        mapView.overlays.add(rotationGestureOverlay)

        setupCompass()
        setupZoomButtons()
    }

    private fun setupCompass() = binding.run {
    /*    compassButton.setOnClickListener {
            if (!rotationGestureOverlay.isEnabled) {
                rotationGestureOverlay.isEnabled = true
                lockerView.visibility = View.INVISIBLE
            }
            mapView.mapOrientation = 0.0f
            compassButton.rotation = 0.0f
        }

        compassButton.setOnLongClickListener {
            if (!rotationGestureOverlay.isEnabled) false
            rotationGestureOverlay.setEnabled(false)
            lockerView.visibility = View.VISIBLE
            true
        }*/
    }

    private fun setupZoomButtons() = binding.run {
     //   zoomInButton.setOnClickListener { mapView.controller.zoomIn() }
       // zoomOutButton.setOnClickListener { mapView.controller.zoomOut() }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause();
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}