package dev.novak.regroup.model

import com.google.android.gms.maps.model.LatLng

const val SEARCH_RADIUS_METRES = "5000"
const val MAX_LOCATIONS = 5

const val NEARBY_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
const val GEOCODING_API = "https://maps.googleapis.com/maps/api/geocode/json"

typealias Address = String

val TEST_RESIDENCES = listOf(
    LatLng(43.51857558108986, -79.87724565848457),
    LatLng(43.516570179968824, -79.85068280201149),
    LatLng(43.50863499193921, -79.8884661605955)
)

val TEST_RESTAURANTS = listOf(
    LatLng(43.53521236939465, -79.91465377866894),
    LatLng(43.53433025147942, -79.91175220876028),
    LatLng(43.52618701528168, -79.9020179097118),
    LatLng(43.52507154108547, -79.90196483702526),
    LatLng(43.5224307858039, -79.90107795815413),
    LatLng(43.52692574022982, -79.88970127271328)
)

val NULL_ISLAND = LatLng(0.0, 0.0)



fun List<LatLng>.getCentre(): LatLng? {
    if (this.isEmpty()) return null

    var lat = 0.0
    var lng = 0.0
    for (location in this) {
        lat += location.latitude
        lng += location.longitude
    }
    return LatLng(lat/this.size, lng/this.size)
}

fun LatLng.asString(): String = "${latitude},${longitude}"

