package dev.novak.regroup.api

import dev.novak.regroup.model.maps.GeocodingResponse
import dev.novak.regroup.model.maps.NearbyResponse
import retrofit2.Response
import retrofit2.http.GET

interface MapsApiInterface {

    @GET("/geocode/json")
    suspend fun getGeocodingResponse(): Response<GeocodingResponse>

    @GET("/place/nearbysearch/json")
    suspend fun getNearbyResponse(): Response<NearbyResponse>

}