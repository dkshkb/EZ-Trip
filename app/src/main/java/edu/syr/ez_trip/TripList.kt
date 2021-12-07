package edu.syr.ez_trip

import com.google.gson.Gson

class TripList {
    var tripList: List<TripData> = Gson().fromJson(trips, Array<TripData>::class.java).asList()
}

val trips = """
    [
        {
            "tripName": "NYC Trip",
            "numDays": 3,
            "tripDetails": "Day1: Central Park, Day2: MET, Day3: Empire State Building"
        },
        
        {
            "tripName": "Syracuse Trip",
            "numDays": 2,
            "tripDetails": "Day1: Green Lake, Day2: Syracuse University"
        }
    ]
    
""".trimIndent()