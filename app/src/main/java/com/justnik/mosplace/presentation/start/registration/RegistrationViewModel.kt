package com.justnik.mosplace.presentation.start.registration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel : ViewModel() {

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


    fun validateInput(email: String, username: String, pass: String, confirmPass: String) {
        validateEmail(email)
        validateUsername(username)
        validatePass(pass)
        validateConfirmPass(pass, confirmPass)
    }

    fun validateEmail(email: String) {
        var error: String? = null
        val regex = Regex("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$")
        if (email.isEmpty()) {
            error = "Empty field"
        } else if (!regex.matches(email)){
            error = "Invalid email"
        }

        _emailErrorState.value = error
    }

    fun validateUsername(username: String) {
        var error: String? = null
        if (username.isEmpty()) {
            error = "Empty field"
        }
        _usernameErrorState.value = error
    }

    fun validatePass(pass: String) {
        var error: String? = null
        if (pass.isEmpty()) {
            error = "Empty field"
        }
        _passErrorState.value = error
    }

    fun validateConfirmPass(pass: String, confirmPass: String) {
        if (confirmPass.isEmpty()) {
            _confirmPassErrorState.value = "Empty field"
        } else if (confirmPass != pass && pass.isNotEmpty()) {
            _passwordsAreSame.value = "Passwords are not same"
        }
    }

    fun resetPassError(){
        _passErrorState.value = null
    }

    fun resetConfirmPassError(){
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
