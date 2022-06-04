package dev.novak.regroup.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dev.novak.regroup.api.MapsApiClient
import dev.novak.regroup.api.MapsApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber


class LocationsViewModel(private val apiKey: String): ViewModel() {

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
                destination.emit(null)
            }
        }
    }

    fun onDestination(action: (destination: Address?) -> Unit) {
        viewModelScope.launch {
            destination.collect {
                action(it)
            }
        }
    }

    private val destination = MutableStateFlow<Address?>(null)
    private val origin = MutableSharedFlow<Address>(MAX_LOCATIONS)
    private val originLatLng = MutableSharedFlow<LatLng>(MAX_LOCATIONS)
    private val mapsApi = MapsApiClient.getInstance().create(MapsApi::class.java)

    private fun buildNearbyRequest(place: LatLng, keyword: String): String = Uri.parse(NEARBY_API)
        .buildUpon()
        .appendQueryParameter("location", place.asString())
        .appendQueryParameter("radius", SEARCH_RADIUS_METRES)
        .appendQueryParameter("keyword", keyword) // For example, Tim Hortons
        .appendQueryParameter("rank_by", "distance")
        .appendQueryParameter("key", apiKey)
        .toString()

    // TODO: the "vicinity" parameter is optional so this can crash
    private fun searchNearby(place: LatLng, keyword: String) {
        Timber.i("searchNearby ${place.asString()} keyword: $keyword")
        viewModelScope.launch {
            val result = mapsApi.getNearbyResponse(place.asString(), keyword, apiKey)
            result.body()?.let {
                if (it.results.isNotEmpty()) {
                    val closestPlace = it.results[0].vicinity
                    Timber.i("searchNearby success, closest place is $closestPlace")
                    destination.emit(closestPlace)
                } else {
                    Timber.w("Search nearby returned zero results")
                    destination.emit("No locations found!")
                }
            } ?: destination.emit("No locations found!")
        }
    }

    // See https://developers.google.com/maps/documentation/geocoding/start
    private fun getOriginLatLng(address: Address) {
        Timber.i("Performing geocoding lat/lng lookup for $address")
        viewModelScope.launch {
            val result = mapsApi.getGeocodingResponse(address, apiKey)
            result.body()?.let {
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