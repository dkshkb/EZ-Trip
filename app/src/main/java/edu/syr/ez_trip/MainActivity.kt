package edu.syr.ez_trip

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var navView : NavigationView
    lateinit var nameTextView : TextView
    lateinit var emailTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLogOutButton = findViewById<Button>(R.id.main_button_logout)
        mLogOutButton.setOnClickListener(View.OnClickListener { logout(mLogOutButton) })

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        nameTextView = navView.getHeaderView(0).findViewById(R.id.user_name)
        emailTextView = navView.getHeaderView(0).findViewById(R.id.user_email)



        // Get name and email
        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Registered Users")
        val child = myRef.child(firebaseUser?.uid as String)

        // Read from the database

        // Update name at Navigation Drawer Header from Firebase Realtime Database
        child.child("fullName").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val name : String? = snapshot.getValue<String>()
                nameTextView.text = name
                Log.d(TAG, "Name is: " + name)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
        // Update email at Navigation Drawer Header from Firebase Realtime Database
        child.child("email").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val email : String? = snapshot.getValue<String>()
                emailTextView.text = email
                Log.d(TAG, "Name is: " + email)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })


        // Set text for name and email in the navigation drawer header

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_home -> nav_home_clicked()
                R.id.nav_planner -> Toast.makeText(applicationContext, "Clicked Planner", Toast.LENGTH_SHORT).show()
                R.id.nav_find_places -> Toast.makeText(applicationContext, "Clicked Find Places", Toast.LENGTH_SHORT).show()
                R.id.nav_map -> nav_map_clicked()
                R.id.nav_travel_videos -> Toast.makeText(applicationContext, "Clicked Travel Videos", Toast.LENGTH_SHORT).show()
                R.id.nav_setting -> Toast.makeText(applicationContext, "Clicked Setting", Toast.LENGTH_SHORT).show()
                R.id.nav_log_out -> logout(mLogOutButton)

            }

            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun logout(view: View?) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun nav_home_clicked(){
        Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun nav_map_clicked(){
        Toast.makeText(applicationContext, "Clicked Map", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

}