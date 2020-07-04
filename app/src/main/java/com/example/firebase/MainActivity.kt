package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
      //  auth.signOut()

        btnRegister.setOnClickListener {
            registerUser()
        }

        btnLogin.setOnClickListener {
            loginUser()
        }
    }



    private fun registerUser() {
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    //withContext cause an error in catch block and because of that i have to omit that
                    // withContext(Dispatchers.Main) {
                    //Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    Log.i("AppMainActivity", e.message.toString())

                    // }
                }
            }
        }
    }

    private fun loginUser() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    // withContext(Dispatchers.Main) {
                    // Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    Log.i("AppMainActivity", e.message.toString())


                }
            }
        }
    }

    private fun checkLoggedInState() {
        if (auth.currentUser == null) { // not logged in
            tvLoggedIn.text = "You are not logged in"
        } else {
            tvLoggedIn.text = "You are logged in!"
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }
}
