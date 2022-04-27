package com.justnik.mosplace.presentation.start.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.usecases.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private var _emailErrorState = MutableStateFlow<String?>(null)
    val emailErrorState = _emailErrorState.asStateFlow()

    private var _usernameErrorState = MutableStateFlow<String?>(null)
    val usernameErrorState = _usernameErrorState.asStateFlow()

    private var _passErrorState = MutableStateFlow<String?>(null)
    val passErrorState = _passErrorState.asStateFlow()

    private var _confirmPassErrorState = MutableStateFlow<String?>(null)
    val confirmPassErrorState = _confirmPassErrorState.asStateFlow()

    private var _passwordsAreSame = MutableStateFlow<String?>(null)
    val passwordsAreSame = _passwordsAreSame.asStateFlow()

    private fun createUser(userFullInfo: UserFullInfo) {
        viewModelScope.launch {
            when (val resource = createUserUseCase(userFullInfo)) {
                is Resource.Success -> {
                    Log.d("RRR", resource.toString())
                }
                is Resource.Error -> {
                    Log.d("RRR", resource.toString())
                }
            }
        }
    }

    fun validateInput(email: String, username: String, pass: String, confirmPass: String) {
        val isEmailValid = validateEmail(email)
        val isUsernameValid = validateUsername(username)
        val isPassValid = validatePass(pass)
        val isConfirmPassValid = validateConfirmPass(pass, confirmPass)

        if (isEmailValid && isUsernameValid && isPassValid && isConfirmPassValid) {
            val userFullInfo = UserFullInfo(username, pass, email)
            createUser(userFullInfo)
        }
    }

    private fun validateEmail(email: String): Boolean {
        var error: String? = null
        val regex = Regex("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$")
        if (email.isEmpty()) {
            error = "Empty field"
        } else if (!regex.matches(email)) {
            error = "Invalid email"
        }

        _emailErrorState.value = error
        return error == null
    }

    private fun validateUsername(username: String): Boolean {
        var error: String? = null
        if (username.isEmpty()) {
            error = "Empty field"
        }
        _usernameErrorState.value = error
        return error == null
    }

    private fun validatePass(pass: String): Boolean {
        var error: String? = null
        if (pass.isEmpty()) {
            error = "Empty field"
        }
        _passErrorState.value = error
        return error == null
    }

    private fun validateConfirmPass(pass: String, confirmPass: String): Boolean {
        if (confirmPass.isEmpty()) {
            _confirmPassErrorState.value = "Empty field"
            return false
        } else if (confirmPass != pass && pass.isNotEmpty()) {
            _passwordsAreSame.value = "Passwords are not same"
            return false
        }
        return true
    }

    fun resetPassError() {
        _passErrorState.value = null
    }

    fun resetConfirmPassError() {
        _confirmPassErrorState.value = null
    }

    fun resetPasswordsNotSameError() {
        _passwordsAreSame.value = null
    }

    fun resetEmailError() {
        _emailErrorState.value = null
    }

    fun resetUsernameError() {
        _usernameErrorState.value = null
    }
}
