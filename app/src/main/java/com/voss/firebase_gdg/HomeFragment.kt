package com.voss.firebase_gdg

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.voss.firebase_gdg.databinding.HomefragmentBinding

class HomeFragment : BaseFragment<HomefragmentBinding>(HomefragmentBinding::inflate) {
    private val navController: NavController by lazy { findNavController() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.signOutBut.setOnClickListener {
            Toast.makeText(this.context,"使用者:${mAuth.currentUser?.email} 已登出",Toast.LENGTH_SHORT).show()
            mAuth.signOut()
            navController.navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
}