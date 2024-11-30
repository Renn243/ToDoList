package com.raihanresa.todolist.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.databinding.ActivityRegisterBinding
import com.raihanresa.todolist.ui.viewmodel.AuthViewModel
import com.raihanresa.todolist.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (password != confirmPassword) {
                val dialog = AlertDialog.Builder(this)
                    .setMessage("Password and Confirm Password must be the same")
                    .setPositiveButton("OK", null)
                    .create()
                dialog.show()
                return@setOnClickListener
            }

            binding.progressIndicator.visibility = View.VISIBLE
            authViewModel.register(username, password).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        val dialog = AlertDialog.Builder(this)
                            .setMessage("Your account has been successfully created")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finishAffinity()
                            }
                            .create()
                        dialog.show()
                    }
                    is ResultState.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        val errorMessage = result.message?.let {
                            try {
                                val json = JSONObject(it)
                                json.getString("errors")
                            } catch (e: JSONException) {
                                it
                            }
                        } ?: "An error occurred"
                        val dialog = AlertDialog.Builder(this)
                            .setMessage(errorMessage)
                            .setPositiveButton("OK", null)
                            .create()
                        dialog.show()
                    }
                }
            }
        }
    }
}
