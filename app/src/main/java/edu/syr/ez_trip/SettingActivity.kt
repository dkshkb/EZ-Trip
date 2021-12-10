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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // Get the firebase realtime database reference based on current user ID
        val fAuth = FirebaseAuth.getInstance()
        val firebaseUser : FirebaseUser? = fAuth.currentUser
        val database = Firebase.database
        val myRef = database.getReference("Registered Users")
        val currUserRef = myRef.child(firebaseUser?.uid as String)

        // Get all the views
        val fullNameText = findViewById<EditText>(R.id.full_name_setting)
        val phoneText = findViewById<EditText>(R.id.phone_setting)
        val settingSubmitButton = findViewById<Button>(R.id.setting_button)
        val settingBackToMain = findViewById<TextView>(R.id.setting_textview_bottom)

        // Click to go back to main
        settingBackToMain.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        currUserRef.child("fullName").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name: String? = snapshot.getValue<String>()
                fullNameText.setText(name)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        currUserRef.child("phoneNum").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val phone: String? = snapshot.getValue<String>()
                phoneText.setText(phone)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        settingSubmitButton.setOnClickListener {

            val fullName = fullNameText.text.toString()
            val phone = phoneText.text.toString()

            // Make sure the user entered is not empty
            if (fullName.isEmpty() || phone.isEmpty()){
                Toast.makeText(this, "Name and phone can't be empty!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                currUserRef.child("fullName").setValue(fullName).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {

                    } else {
                        Toast.makeText(
                            this,
                            "Error! " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                currUserRef.child("phoneNum").setValue(phone).addOnCompleteListener{ task ->
                    if (task.isSuccessful) {

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
    }
}