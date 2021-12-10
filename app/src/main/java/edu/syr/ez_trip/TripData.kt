package edu.syr.ez_trip

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//data class TripData(
//   @SerializedName("tripName") var tripName : String,
//   @SerializedName("numDays") var numDays : Int,
//   @SerializedName("tripDetails") var tripDetails : String,
//   @SerializedName("dailyPlans") var dailyPlans : ArrayList<String>,
//   @SerializedName("checked") var checked: Boolean
//)

class TripData(tripName: String, tripDetails : String, dailyPlans : String){

   var tripName : String
   var tripDetails : String
   var dailyPlans : String
   var checked : Boolean = false

   init{
      this.tripName = tripName
      this.tripDetails = tripDetails
      this.dailyPlans = dailyPlans
   }


   fun copy(): TripData {
      return TripData(this.tripName, this.tripDetails, this.dailyPlans)
   }
}