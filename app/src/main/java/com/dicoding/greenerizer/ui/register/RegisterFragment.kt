package com.dicoding.greenerizer.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        name = binding.nameInputEditText.text.toString()
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

        binding.signupButton.setOnClickListener {
            signUpUser()
        }

        binding.questionLogin.setOnClickListener{
            view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun isEmailValid(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setMyButtonEnabled() {
        name = binding.nameInputEditText.text.toString()
        email = binding.emailInputEditText.text.toString()
        password = binding.passwordInputEditText.text.toString()
        binding.signupButton.isEnabled = name.isNotEmpty() && email.isNotEmpty() && password.length >= 8
    }

    private fun signUpUser(){
        binding.progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) {
            if(it.isSuccessful) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.success_signup), Toast.LENGTH_SHORT).show()
                view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.failed_signup), Toast.LENGTH_SHORT).show()
            }
        }
    }

}