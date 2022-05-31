package dev.novak.regroup.model.maps

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    val results: List<GeocodingResult>,
    val status:  String
    )


data class GeocodingResult(
    @SerializedName("address_components")
    val addressComponents: List<AddressComponent>,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    val geometry: Geometry,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCode,
    val types: List<String>,
    )


