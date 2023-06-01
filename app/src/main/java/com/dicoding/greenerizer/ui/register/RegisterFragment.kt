package com.dicoding.greenerizer.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dicoding.greenerizer.R
import com.dicoding.greenerizer.data.local.User
import com.dicoding.greenerizer.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

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
        playAnimation()

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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            ActivityCompat.finishAffinity(requireActivity())
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.title, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameEdt = ObjectAnimator.ofFloat(binding.nameInputLayout, View.ALPHA, 1f).setDuration(500)
        val emailEdt = ObjectAnimator.ofFloat(binding.emailInputLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEdt = ObjectAnimator.ofFloat(binding.passwordInputLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val signin = ObjectAnimator.ofFloat(binding.questionLogin, View.ALPHA, 1f).setDuration(500)
        val image = ObjectAnimator.ofFloat(binding.image, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(nameEdt, emailEdt, passwordEdt, signup, signin, image)
            start()
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
        db = Firebase.database
        val userRef = db.reference.child(USER_CHILD)

        binding.progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) {
            if(it.isSuccessful) {
                val userSignedUp = User(
                    name, email, password, 0
                )
                Log.d("Daftar", userSignedUp.toString())
                userRef.child(it.result.user?.uid.toString()).setValue(userSignedUp) { error, _ ->
                    if(error != null) {
                        Toast.makeText(requireContext(), resources.getString(R.string.failed_signup) + error.message, Toast.LENGTH_SHORT).show()
                    } else {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), resources.getString(R.string.success_signup), Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), resources.getString(R.string.failed_signup), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val USER_CHILD = "users"
    }

}