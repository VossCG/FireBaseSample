package com.voss.firebase_gdg

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.google.firebase.auth.FirebaseAuth
import com.voss.firebase_gdg.databinding.SingupfragmentBinding

class SignUpFragment : BaseFragment<SingupfragmentBinding>(SingupfragmentBinding::inflate) {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val navController by lazy { findNavController() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBut.setOnClickListener {
            val pair = getInputFromEditText()
            if (pair != null)
                setFireBaseSingUp(pair.first, pair.second)
            else Toast.makeText(this.context, "Please filled out the fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

//
//    private fun checkLogin() {
//        val user = mAuth.currentUser
//        Log.d("Login", user.toString())
//        if (user != null && user.isEmailVerified) {
//            Toast.makeText(this.context, "已有登入紀錄", Toast.LENGTH_SHORT).show()
//            navController.navigate(R.id.action_loginFragment_to_homeFragment)
//        }
//    }

    private fun getInputFromEditText(): Pair<String, String>? {
        val email = binding.accountRegisterEdittext.text.toString()
        val password = binding.passwordRegisterEdittext.text.toString()

        return if (email.isNotEmpty() && password.isNotEmpty()) {
            Pair(email, password)
        } else null
    }

    private fun setFireBaseSingUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this.context, "註冊成功", Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this.context)
                    .setTitle("驗證信箱")
                    .setMessage("需驗證完信箱 才能登入 \n是否現在進行驗證?")
                    .setPositiveButton("是,現在進行") { _, _ ->
                        sendVerifiedEmail()
                        navController.navigate(R.id.action_signUpFragment_to_loginFragment)
                    }.setNegativeButton("否,稍後再說", null)
                    .show()
            } else Toast.makeText(this.context, it.exception?.message, Toast.LENGTH_SHORT).show()
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