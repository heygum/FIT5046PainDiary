package com.example.fit5046paindiary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fit5046paindiary.databinding.ActivitySignInBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignInBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignInBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUp.setOnClickListener {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

        val prefs = getPreferences(Context.MODE_PRIVATE)
        val rememberPw = prefs.getBoolean("rememberPw",false)

        if(rememberPw)
        {
            val email = prefs.getString("email","")
            val password = prefs.getString("password","")
            binding.accountEdit.setText(email)
            binding.passwordEdit.setText(password)
            binding.rememberPw.isChecked = true
        }

        binding.login.setOnClickListener {
            val email = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()

            if(email != "" && password != "")
            {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val editor = prefs.edit()
                    if(binding.rememberPw.isChecked)
                    {
                        editor.putBoolean("rememberPw",true)
                        editor.putString("email",email)
                        editor.putString("password",password)
                    }
                    else
                    {
                        editor.clear()
                    }
                    editor.apply()
                    Toast.makeText(this, "success!.", Toast.LENGTH_SHORT).show()
                    binding.accountEdit.setText("")
                    binding.passwordEdit.setText("")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(
                        this,
                        "Please check your Email and Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            }
            else
            {
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    binding.accountEdit.setError("Please enter the correct Email address.")
                Toast.makeText(
                    this,
                    "Please check your Email and Password.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}