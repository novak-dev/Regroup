package dev.novak.regroup.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dev.novak.regroup.api.MapsApiClient
import dev.novak.regroup.api.MapsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

const val MAX_LOCATIONS = 5

class LocationsViewModel: ViewModel() {

    fun addOrigin(orig: String) = viewModelScope.launch { origin.emit(orig) }

    fun startSearch(keyword: String) {
        viewModelScope.launch {
            val originList = originLatLng.replayCache
            Timber.i("Origin list: $originList")
            val centre = originList.getCentre()
            if (centre != null) {
                Timber.i("Centre of search area: ${centre.asString()}")
                searchNearby(centre, keyword)
            } else {
                Timber.e("Centre point is null! Not able to search: $originList")
                destination.emit(LatLng(0.0, 0.0))
            }
        }
    }


    fun onDestination(action: (destination: LatLng) -> Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {  // TODO: refactor - getMapAsync must run on main
                destination.collect { action(it) }
            }
        }
    }

    fun setKey(mapsApiKey: String) {
        Timber.i("Setting API key")
        apiKey = mapsApiKey
    }

    private val destination = MutableStateFlow(LatLng(0.0, 0.0))
    private val origin = MutableSharedFlow<Address>(MAX_LOCATIONS)
    private val originLatLng = MutableSharedFlow<LatLng>(MAX_LOCATIONS)
    private val mapsApi = MapsApiClient.getInstance().create(MapsApi::class.java)
    // TODO: lets disable the buttons if the api key is not set to prevent weird race condition
    private lateinit var apiKey: String

    // TODO: handle edge cases
    private fun searchNearby(place: LatLng, keyword: String) {
        Timber.i("searchNearby ${place.asString()} keyword: $keyword")
        viewModelScope.launch {
            val response = mapsApi.getNearbyResponse(place.asString(), keyword, apiKey)
            response.body()?.let {
                val result = it.results[0].geometry!!.location
                destination.emit(LatLng(result.lat, result.lng))
            } ?: destination.emit(LatLng(0.0, 0.0))
        }
    }

    // See https://developers.google.com/maps/documentation/geocoding/start
    private fun getOriginLatLng(address: Address) {
        Timber.i("Performing geocoding lat/lng lookup for $address")
        viewModelScope.launch {
            val response = mapsApi.getGeocodingResponse(address, apiKey)
            response.body()?.let {
                if (it.results.isNotEmpty()) {
                    val latLingLiteral = it.results[0].geometry.location
                    val latLng = LatLng(latLingLiteral.lat, latLingLiteral.lng)
                    Timber.i("Geocoding lookup success. Coordinates: ${latLng.asString()}")
                    originLatLng.emit(latLng)
                } else {
                    Timber.w("Geocoding lookup returned zero results")
                }
            }
        }
    }


    init {
        viewModelScope.launch {
            origin.collect {
                Timber.i("Got origin: $it")
                getOriginLatLng(it)
            }
        }
        viewModelScope.launch {
            originLatLng.collect {
                Timber.i("Got origin coordinates: ${it.asString()}")
            }
        }
        viewModelScope.launch {
            destination.collect {
                Timber.i("Got destination address: $it")
            }
        }
    }

}