package edu.syr.ez_trip

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TripData(
   @SerializedName("tripName") var tripName : String,
   @SerializedName("numDays") var numDays : Int,
   @SerializedName("tripDetails") var tripDetails : String,
   @SerializedName("checked") var checked: Boolean
)