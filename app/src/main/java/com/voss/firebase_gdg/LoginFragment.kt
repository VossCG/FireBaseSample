package com.voss.firebase_gdg

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.voss.firebase_gdg.databinding.LoginfragmentBinding

class LoginFragment : BaseFragment<LoginfragmentBinding>(LoginfragmentBinding::inflate) {
    private var userCorrect = false
    private var isVerified = false
    private val navController by lazy { findNavController() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.loginBut.setOnClickListener {
            val pair: Pair<String, String>? = getInput()
            if (pair != null)
                setFireBaseLogin(pair.first, pair.second)
            else Toast.makeText(this.context, "Please filled out the fields", Toast.LENGTH_SHORT)
                .show()
        }
        binding.toSingUpBut.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }

    private fun navigateToHome() {
        if (userCorrect && isVerified) {
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        } else {
            Toast.makeText(this.context, "信箱未驗證過", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLogin() {
        val user = mAuth.currentUser
        Log.d("Login", user.toString())
        if (user != null) {
            Toast.makeText(this.context, "已有登入紀錄", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    private fun getInput(): Pair<String, String>? {
        val email = binding.accountEdittext.text.toString()
        val password = binding.passwordEdittext.text.toString()

        return if (email.isNotEmpty() && password.isNotEmpty()) {
            Pair(email, password)
        } else null
    }

    private fun setFireBaseLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this.context, "login Successful", Toast.LENGTH_SHORT).show()
                navigateToHome()
            } else
                Toast.makeText(this.context, task.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }
}