package dev.novak.regroup.model.maps

import com.google.gson.annotations.SerializedName

data class AddressComponent(
    @SerializedName("long_name")
    val longName: String,
    @SerializedName("short_name")
    val shortName: String,
    val types: List<String>
)

data class LatLngLiteral(
    val lat: Double,
    val lng: Double
)

data class Bounds(
    val northeast: LatLngLiteral,
    val southwest: LatLngLiteral
)

data class Geometry(
    val location: LatLngLiteral,
    @SerializedName("location_type")
    val locationType: String?,
    val viewport: Bounds,
)

data class PlusCode(
    @SerializedName("compound_code")
    val compoundCode: String?,
    @SerializedName("global_code")
    val globalCode: String
)

fun LatLngLiteral.asString() = "${lat},${lng}"

