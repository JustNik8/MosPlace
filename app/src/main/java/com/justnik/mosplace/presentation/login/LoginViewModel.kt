package com.justnik.mosplace.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.network.authmodels.LoginInfo
import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.usecases.auth.LoginUserUseCase
import com.justnik.mosplace.domain.usecases.auth.ValidateEmail
import com.justnik.mosplace.domain.usecases.auth.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val loginUserUseCase: LoginUserUseCase
) : ViewModel() {
    private var _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState = _loginFormState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.EmailChanged -> {
                _loginFormState.value =
                    loginFormState.value.copy(email = event.email, emailError = null)
            }
            is LoginFormEvent.PasswordChanged -> {
                _loginFormState.value =
                    loginFormState.value.copy(password = event.password, passwordError = null)
            }
            is LoginFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail(loginFormState.value.email)
        val passwordResult = validatePassword(loginFormState.value.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }
        if (hasError) {
            _loginFormState.value = loginFormState.value.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            return
        }


        loginUser()

    }

    private fun loginUser() {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            val email = loginFormState.value.email
            val password = loginFormState.value.password
            val loginInfo = LoginInfo(email, password)
            val response = loginUserUseCase(loginInfo)
            _uiState.value = UiState(isLoading = false)
            when (response) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success(UiText.DynamicText("Success")))
                }
                is Resource.Error -> {
                    val message = response.message
                    message?.let {
                        validationEventChannel.send(ValidationEvent.Error(it))
                        return@launch
                    }
                    validationEventChannel.send(ValidationEvent.Error(UiText.StringResource(R.string.unknown_error)))
                }
            }
        }
    }

    sealed class ValidationEvent {
        class Success(val successMessage: UiText) : ValidationEvent()
        class Error(val errorMessage: UiText) : ValidationEvent()
    }

    data class UiState(
        val isLoading: Boolean = false
    )
}
