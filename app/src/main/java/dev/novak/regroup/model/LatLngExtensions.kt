package dev.novak.regroup.model

import com.google.android.gms.maps.model.LatLng

typealias Address = String

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

