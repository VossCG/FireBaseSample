package com.voss.firebase_gdg

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
                navController.navigate(R.id.action_signUpFragment_to_loginFragment)
            } else Toast.makeText(this.context, it.exception?.message, Toast.LENGTH_SHORT).show()
        }
    }
}