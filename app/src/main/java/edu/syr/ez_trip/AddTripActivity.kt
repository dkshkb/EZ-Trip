package edu.syr.ez_trip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class AddTripActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trip)

        // Get all the views
        val tripNameText = findViewById<EditText>(R.id.trip_name_text)
        val tripDetailsText = findViewById<EditText>(R.id.trip_details_text)
        val tripDailyPlanText = findViewById<EditText>(R.id.trip_daily_plans_text)
        val addTripButton = findViewById<Button>(R.id.add_new_trip_button)
        val backToPlannerText = findViewById<TextView>(R.id.back_to_planner_text)

        // Get the firebase realtime database reference based on current user ID
        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Trip Data")
        val tripListRef = myRef.child(firebaseUser?.uid as String)

        backToPlannerText.setOnClickListener{
            val intent = Intent(this, PlannerActivity::class.java)
            startActivity(intent)
        }

        addTripButton.setOnClickListener{
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
        }


    }
}