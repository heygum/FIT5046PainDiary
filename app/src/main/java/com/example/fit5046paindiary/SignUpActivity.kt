package com.example.fit5046paindiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.fit5046paindiary.databinding.ActivitySignUpBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding  = ActivitySignUpBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.confirm.setOnClickListener {
            //binding.passwordEdit.setError(null)
            //binding.accountEdit.setError(null)
            val email = binding.accountEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            if(email != "" && password != "" && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && Password_validation(password))
            {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,"success!.",Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this,"Fail to sign up.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this,"Please enter email and password correctly.",Toast.LENGTH_SHORT).show()
                if(!Password_validation(password))
                    binding.passwordEdit.setError("Password at least 6 words including 1 Uppercase, 1 Number and 1 Symbol")
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    binding.accountEdit.setError("Please enter the correct Email address.")
                else
                    ExistedEmail(email)
            }
        }

        binding.cancel.setOnClickListener {
            finish()
        }
    }

    fun Password_validation(password: String):Boolean{
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$"
        val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun ExistedEmail(email : String) {
        auth.fetchSignInMethodsForEmail(email).addOnSuccessListener {
                result ->
            val signInMethods = result.signInMethods!!
            if (signInMethods.isEmpty()) {
                binding.accountEdit.setError(null)
            } else  {
                binding.accountEdit.setError("This Email is already exist.")
            }
        }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}

