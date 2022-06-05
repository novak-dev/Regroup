package dev.novak.regroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.novak.regroup.model.LocationsViewModel
import timber.log.Timber


class ResultFragment: Fragment() {

    private val locationsViewModel: LocationsViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private val mapViewBundleKey = "MapViewBundleKey"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val mapviewBundle = when (savedInstanceState) {
            null -> null
            else -> savedInstanceState.getBundle(mapViewBundleKey)
        }

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(mapviewBundle)
        locationsViewModel.onDestination { latLng ->
            Timber.i("Got destination $latLng")
            mapView.getMapAsync{ map ->
                Timber.i("Map is ready")
                map.clear()
                map.addMarker(MarkerOptions().position(latLng).title("Destination"))
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                map.setMinZoomPreference(20.0f)
                map.setMaxZoomPreference(20.0f)
            }
        }
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(mapViewBundleKey)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(mapViewBundleKey, mapViewBundle)
        }
        mapView.onSaveInstanceState(mapViewBundle)
    }


   override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


}