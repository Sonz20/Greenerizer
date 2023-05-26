package com.dicoding.greenerizer.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var email: String
    private lateinit var password: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        email = binding.emailInputEditText.text.toString()
        password = binding.passwordInputEditText.text.toString()

        setMyButtonEnabled()

        // Email Validation
        binding.emailInputEditText.doOnTextChanged { text, _, _, _ ->
            if(!isEmailValid(text.toString())) {
                binding.emailInputEditText.error = getString(R.string.invalid_email)
            } else {
                binding.emailInputEditText.error = null
            }
        }

        // Check Password length >= 8
        binding.passwordInputEditText.doOnTextChanged { text, _, _, _ ->
            if (text!!.length < 8) {
                binding.passwordInputEditText.error = getString(R.string.invalid_password)
            } else {
                binding.passwordInputEditText.error = null
                setMyButtonEnabled()
            }
        }

        binding.questionRegister.setOnClickListener{
            view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.signinButton.setOnClickListener{
            signInUser()
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setMyButtonEnabled() {
        email = binding.emailInputEditText.text.toString()
        password = binding.passwordInputEditText.text.toString()
        binding.signinButton.isEnabled = email.isNotEmpty() && password.length >= 8
    }

    private fun signInUser() {
        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.signin_success), Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                checkUser(user)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.signin_failed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkUser(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            view?.findNavController()?.navigate(R.id.action_loginFragment_to_navigation_home)
        }
    }
}