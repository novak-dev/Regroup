package dev.novak.regroup.model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.Fuel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


class LocationsViewModel(private val apiKey: String): ViewModel() {
    fun addOrigin(orig: String) = viewModelScope.launch { origin.emit(orig) }

    fun startSearch(keyword: String) {
        viewModelScope.launch {
            val originList = originLatLng.replayCache // Yeah it took me 2 hours to find this..
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
        val request = buildNearbyRequest(place, keyword)
        Timber.i("searchNearby request: $request")
        Fuel.get(request).response { _, _, result ->
            val (bytes, _) = result
            if (bytes != null) {
                val json = JSONObject(String(bytes))
                val results = json.getJSONArray("results")
                Timber.i("Maps API results: $results")
                if (results.length() > 0) {
                    val closestPlace = results.getJSONObject(0)
                    viewModelScope.launch { destination.emit(closestPlace.getString("vicinity")) }
                } else {
                    viewModelScope.launch { destination.emit("No locations found!") }
                }
            } else {
                Timber.e("Got null response in searchNearby!")
            }
        }
    }

    // See https://developers.google.com/maps/documentation/geocoding/start
    private fun buildGeocodingRequest(address: Address): String = Uri.parse(GEOCODING_API)
        .buildUpon()
        .appendQueryParameter("address", address)
        .appendQueryParameter("key", apiKey)
        .toString()


    // TODO: error handling
    private fun getOriginLatLng(address: Address) {
        Timber.i("Performing latitude/longitude lookup for $address")
        val request = buildGeocodingRequest(address)
        Timber.i("Geocoding request: $request")
        Fuel.get(request).response { _, _, result ->
            val (bytes, _) = result
            if (bytes != null) {
                val json = JSONObject(String(bytes))
                Timber.i("getOriginLatLng response: $json")
                val loc = json
                    .getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location")
                val latLng = LatLng(
                    loc.getString("lat").toDouble(),
                    loc.getString("lng").toDouble()
                )
                Timber.i("Geocoding lookup success. Coordinates: ${latLng.asString()}")
                viewModelScope.launch { originLatLng.emit(latLng) }
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