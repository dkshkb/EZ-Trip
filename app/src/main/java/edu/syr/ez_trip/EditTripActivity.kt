package edu.syr.ez_trip

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class EditTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_trip)

        // Get all the views
        val tripNameText = findViewById<EditText>(R.id.trip_name_text_edit)
        val tripDetailsText = findViewById<EditText>(R.id.trip_details_text_edit)
        val tripDailyPlanText = findViewById<EditText>(R.id.trip_daily_plans_text_edit)
        val editTripButton = findViewById<Button>(R.id.edit_trip_button)
        val backToPlannerText = findViewById<TextView>(R.id.back_to_planner_text_edit)

        // Get the firebase realtime database reference based on current user ID
        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Trip Data")
        val tripListRef = myRef.child(firebaseUser?.uid as String)

        val tripNameSelected = intent.getStringExtra("tripNameSelected")
        if (tripNameSelected != null) {
            val currTripRef = tripListRef.child(tripNameSelected)

            tripNameText.setText(tripNameSelected)

            currTripRef.child("tripDetails").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tripDetails: String? = snapshot.getValue<String>()
                    tripDetailsText.setText(tripDetails)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

            currTripRef.child("dailyPlans").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dailyPlans : String? = snapshot.getValue<String>()
                    tripDailyPlanText.setText(dailyPlans)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
        // Fill the Edittext based on the selected trip
//        tripNameText.text =
//        tripDetailsText.text =
//        tripDailyPlanText.text =


        backToPlannerText.setOnClickListener{
            val intent = Intent(this, PlannerActivity::class.java)
            startActivity(intent)
        }

        editTripButton.setOnClickListener{
            if (tripNameText.text.toString().length != 0) {
                val tripName = tripNameText.text.toString()
                val tripDetails = tripDetailsText.text.toString()
                val tripDailyPlans = tripDailyPlanText.text.toString()
                val newTrip = TripData(tripName, tripDetails, tripDailyPlans)


                tripListRef.child(tripName).setValue(newTrip).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(this, "New Trip Added Successfully", Toast.LENGTH_SHORT)
                            .show()

                        val intent = Intent(applicationContext, PlannerActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this,
                            "Error! " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // Check if the user also edit the trip name,
                // If not, just update in the firebase
                // If the trip name is changed, I also need to delete the one with original name
                // Since I use trip name as key in the Json Tree
                if (!tripName.equals(tripNameSelected)) {
                    if (tripNameSelected != null) {
                        tripListRef.child(tripNameSelected).setValue(null)
                    }
                }

            } else {
                Toast.makeText(this, "Trip Name can't be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }



    }
}