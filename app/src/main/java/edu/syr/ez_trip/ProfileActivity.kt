package edu.syr.ez_trip

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nameText = findViewById<TextView>(R.id.profile_name)
        val emailText = findViewById<TextView>(R.id.profile_email)
        val phoneText = findViewById<TextView>(R.id.profile_phone)
        val numPlansText = findViewById<TextView>(R.id.profile_num_plans)

        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Registered Users")
        val child = myRef.child(firebaseUser?.uid as String)
        val tripListRef = database.getReference("Trip Data").child(firebaseUser?.uid as String)

        // Update Profile Activity from firebase
        // Update name
        child.child("fullName").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val name : String? = snapshot.getValue<String>()
                nameText.text = name
                Log.d(ContentValues.TAG, "Name is: " + name)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })
        // Update email
        child.child("email").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val email : String? = snapshot.getValue<String>()
                emailText.text = email
                Log.d(ContentValues.TAG, "Name is: " + email)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

        // Update Phone
        child.child("phoneNum").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val phone : String? = snapshot.getValue<String>()
                phoneText.text = phone
                Log.d(ContentValues.TAG, "Name is: " + phone)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })


        // Update the number of plans(trips)
        tripListRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var numPlans = snapshot.childrenCount.toString()
                numPlansText.text = numPlans
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

    }
}