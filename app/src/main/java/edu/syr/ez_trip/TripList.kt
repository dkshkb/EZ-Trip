package edu.syr.ez_trip

import com.google.gson.Gson

class TripList {
    var tripList: List<TripData> = Gson().fromJson(trips, Array<TripData>::class.java).asList()
}

val trips = """
    [
        {
            "tripName": "NYC Trip",
            "tripDetails": "That is my first trip to NYC",
            "dailyPlans":"Central Park;MET;Empire State Building"
        },
        
        {
            "tripName": "Syracuse Trip",
            "tripDetails": "Syracuse is known for heavy snow",
            "dailyPlans":"Green Lake;Syracuse University"
        }
    ]
    
""".trimIndent()