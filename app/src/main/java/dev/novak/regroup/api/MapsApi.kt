package dev.novak.regroup.api

import dev.novak.regroup.model.SEARCH_RADIUS_METRES
import dev.novak.regroup.model.maps.GeocodingResponse
import dev.novak.regroup.model.maps.NearbyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsApi {

    @GET("api/geocode/json?")
    suspend fun getGeocodingResponse(
        @Query("address") address: String,
        @Query("key") key: String): Response<GeocodingResponse>

    @GET("api/place/nearbysearch/json?rank_by=distance&radius=${SEARCH_RADIUS_METRES}")
    suspend fun getNearbyResponse(
        @Query("location") location: String,
        @Query("keyword") keyword: String,
        @Query("key") key: String): Response<NearbyResponse>
}