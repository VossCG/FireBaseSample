package com.voss.firebase_gdg

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.voss.firebase_gdg.databinding.LoginfragmentBinding
import java.util.*

class LoginFragment : BaseFragment<LoginfragmentBinding>(LoginfragmentBinding::inflate) {
    private val navController by lazy { findNavController() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onStart() {
        super.onStart()
        checkLogin()
        Log.d("LoginFragment","user:${mAuth.currentUser} verified:${mAuth.currentUser?.isEmailVerified}")
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

    private fun checkLogin() {
        val user = mAuth.currentUser
        Log.d("Login", user.toString())
        if (user != null && user.isEmailVerified) {
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

    // 登入的時候 verified 才變成true ， 不是點選信件完成才更動
    private fun setFireBaseLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToHome()
            } else
                Toast.makeText(this.context, task.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToHome() {
        // 信箱是否被驗證過
        if (mAuth.currentUser?.isEmailVerified!!) {
            Toast.makeText(this.context, "login Successful", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        } else {
            Toast.makeText(this.context, "信箱未驗證過", Toast.LENGTH_SHORT).show()

            AlertDialog.Builder(this.context)
                .setTitle("驗證信箱")
                .setMessage("需驗證完信箱 才能繼續使用 \n 是否現在進行驗證?")
                .setPositiveButton("Yes") { _, _ ->
                    sendVerifiedEmail()
                }.setNegativeButton("No", null)
                .show()
        }
    }

    private fun sendVerifiedEmail() {
        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this.context, "開始驗證 email", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this.context, "驗證過程失敗", Toast.LENGTH_SHORT).show()
        }
    }
}