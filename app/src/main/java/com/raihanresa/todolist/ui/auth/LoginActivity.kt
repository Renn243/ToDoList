package com.raihanresa.todolist.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.raihanresa.todolist.data.local.UserPreference
import com.raihanresa.todolist.data.remote.ResultState
import com.raihanresa.todolist.databinding.ActivityLoginBinding
import com.raihanresa.todolist.ui.viewmodel.AuthViewModel
import com.raihanresa.todolist.ui.viewmodel.ViewModelFactory
import com.raihanresa.todolist.ui.main.MainActivity
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            authViewModel.login(username, password).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        val userData = result.data.data
                        val userId = userData?.id
                        val username = userData?.username

                        lifecycleScope.launch {
                            if (userId != null && username != null) {
                                userPreference.saveData(userId, username)
                            }
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
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
