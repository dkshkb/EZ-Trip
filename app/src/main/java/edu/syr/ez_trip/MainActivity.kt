package edu.syr.ez_trip

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
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
    lateinit var profilePicture : de.hdodenhof.circleimageview.CircleImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mLogOutButton = findViewById<Button>(R.id.main_button_logout)
        mLogOutButton.setOnClickListener(View.OnClickListener { logout(mLogOutButton) })

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        nameTextView = navView.getHeaderView(0).findViewById(R.id.user_name)
        emailTextView = navView.getHeaderView(0).findViewById(R.id.user_email)
        profilePicture = navView.getHeaderView(0).findViewById(R.id.nav_header_profile_picture)



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

                R.id.nav_home -> navHomeClicked()
                R.id.nav_planner -> navPlannerClicked()
                R.id.nav_find_places -> navFindPlacesClicked()
                R.id.nav_map -> navMapClicked()
                R.id.nav_travel_videos -> navVideoClicked()
                R.id.nav_setting -> navSettingClicked()
                R.id.nav_log_out -> logout(mLogOutButton)

            }

            true
        }


        // When clicking on the profile picture at navigation drawer, it will go to profile activity
        profilePicture.setOnClickListener{
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        nameTextView.setOnClickListener{

            // Add a dialog to modify user full name from firebase
            var builder : AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Do you want to change your name to the following input?")
            builder.setTitle("Change Name")
            var edittext = EditText(this@MainActivity)
            edittext.setText(nameTextView.text.toString())
            builder.setView(edittext)
            builder.setPositiveButton("Yes") { dialog, id ->
                var newName : String = edittext.text.toString()
                Log.d(TAG, "new name: " + newName)

                if (newName.isEmpty()){
                    Toast.makeText(this, "Name can't be empty!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    child.child("fullName").setValue(newName).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Name modified successfully!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                this,
                                "Error! " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            builder.setNegativeButton("No") { dialog, id ->

                Log.d(TAG, "Negative Button")

//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
            }

            builder.show()
        }

        emailTextView.setOnClickListener{
            Toast.makeText(applicationContext, "You can't modify your email", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun logout(view: View?) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navHomeClicked(){
        Toast.makeText(applicationContext, "Clicked Home", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun navMapClicked(){
        Toast.makeText(applicationContext, "Clicked Map", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun navVideoClicked(){
        Toast.makeText(applicationContext, "Clicked Travel Video", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, VideoActivity::class.java)
        startActivity(intent)
    }

    private fun navPlannerClicked(){
        Toast.makeText(applicationContext, "Clicked Planner", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PlannerActivity::class.java)
        startActivity(intent)
    }

    private fun navFindPlacesClicked(){
        Toast.makeText(applicationContext, "Clicked Find Places", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, FindPlacesActivity::class.java)
        startActivity(intent)
    }

    private fun navSettingClicked(){
        Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

}