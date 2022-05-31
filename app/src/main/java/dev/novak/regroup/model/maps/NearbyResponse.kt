package dev.novak.regroup.model.maps

import com.google.gson.annotations.SerializedName

data class NearbyResponse(
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    val results: List<SearchPlace>,
    val status: String,
    @SerializedName("error_message")
    val errorMessage: String?,
    @SerializedName("info_messages")
    val infoMessages: List<String>?,
    @SerializedName("next_page_token")
    val nextPageToken: String?
    )

data class SearchPlace(
    @SerializedName("address_components")
    val addressComponents: List<AddressComponent>?,
    @SerializedName("adr_address")
    val adrAddress: String?,
    @SerializedName("business_status")
    val businessStatus: String?,
    @SerializedName("formatted_address")
    val formattedAddress: String?,
    @SerializedName("formatted_phone_number")
    val formattedPhoneNumber: String?,
    val geometry: Geometry?,
    val icon: String?,
    @SerializedName("icon_background_color")
    val iconBackgroundColor: String?,
    @SerializedName("icon_mask_base_uri")
    val iconMaskBaseUri: String?,
    @SerializedName("international_phone_number")
    val internationalPhoneNumber: String?,
    val name: String?,
    @SerializedName("opening_hours")
    val openingHours: PlaceOpeningHours?,
    val photos: List<PlacePhoto>?,
    @SerializedName("place_id")
    val placeId: String?,
    @SerializedName("plus_code")
    val plusCode: PlusCode?,
    @SerializedName("price_level")
    val priceLevel: Int?,
    val rating: Double?,
    val reviews: List<PlaceReview>?,
    val types: List<String>?,
    val url: String?,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int?,
    @SerializedName("utc_offset")
    val utcOffset: Int?,
    val vicinity: String?,
    val website: String?
)

data class PlaceOpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean?,
    val periods: List<PlaceOpeningHoursPeriod>?,
    @SerializedName("weekday_text")
    val weekdayText: List<String>?
)

data class PlaceOpeningHoursPeriod(
    val open: PlaceOpeningHoursPeriodDetail,
    val close: PlaceOpeningHoursPeriodDetail?
)

data class PlaceOpeningHoursPeriodDetail(
    val day: Int,
    val time: String
)


data class PlacePhoto(
    val height: Int,
    val width: Int,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String,
)

data class PlaceReview(
    @SerializedName("author_name")
    val authorName: String,
    val rating: Int,
    @SerializedName("relative_time_description")
    val relativeTimeDescription: String,
    val time: Int,
    @SerializedName("author_url")
    val authorUrl: String?,
    val language: String?,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String?,
    val text: String?
    )