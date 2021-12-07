package edu.syr.ez_trip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val mFullName = findViewById<EditText>(R.id.full_name_register)
        val mEmail = findViewById<EditText>(R.id.email_register)
        val mPassword = findViewById<EditText>(R.id.register_pwd1)
        val mPhone = findViewById<EditText>(R.id.register_phone)
        val mRegisterButton = findViewById<Button>(R.id.register_button)
        val fAuth = FirebaseAuth.getInstance()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val mLoginButton = findViewById<TextView>(R.id.register_textview_bottom)


        // If the user has already logged in, send them to main activity directly
        if (fAuth.currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish() // onDestroyed() will be called
        }

        mRegisterButton.setOnClickListener(View.OnClickListener {
            val email = mEmail.text.toString().trim { it <= ' ' } //usr trim to format the data
            val pwd = mPassword.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                mEmail.error = "Email is required"
                return@OnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                mPassword.error = "Password is required"
                return@OnClickListener
            }
            if (pwd.length < 6) {
                mPassword.error = "Password has to contain at least 6 characters"
                return@OnClickListener
            }

            // The above checks are used to make sure the input is valid
            progressBar.visibility = View.VISIBLE

            //register the user into firebase
            fAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
                if (task.isSuccessful) {


                    val firebaseUser : FirebaseUser? = fAuth.currentUser
                    // Store User info to Firebase Realtime Database
                    val writeUserDetails =
                        ReadWriteUserDetails(mFullName.text.toString(), mEmail.text.toString(),mPhone.text.toString())

                    val referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users")

                    referenceProfile.child(firebaseUser?.uid as String).setValue(writeUserDetails).addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT)
                                .show()

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Error! " + task.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {
                    Toast.makeText(
                        this,
                        "Error! " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        mLoginButton.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}