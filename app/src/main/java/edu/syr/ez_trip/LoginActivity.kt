package edu.syr.ez_trip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mEmail = findViewById<TextView>(R.id.email_login)
        val mPassword = findViewById<EditText>(R.id.login_pwd)
        val fAuth = FirebaseAuth.getInstance()
        val mLoginButton = findViewById<Button>(R.id.button_login)
        val mCreateButton = findViewById<TextView>(R.id.login_textview_bottom)
        mLoginButton.setOnClickListener(View.OnClickListener {
            val email = mEmail.getText().toString().trim { it <= ' ' } //usr trim to format the data
            val pwd: String = mPassword.getText().toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(pwd)) {
                mPassword.setError("Password is required")
                return@OnClickListener
            }
            if (pwd.length < 6) {
                mPassword.setError("Password has to contain at least 6 characters")
                return@OnClickListener
            }

            // The above checks are used to make sure the input is valid

            //rogressBar.setVisibility(View.VISIBLE);

            //login the user into firebase
            fAuth!!.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error! " + task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        mCreateButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        })
    }
}