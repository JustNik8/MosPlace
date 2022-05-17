package com.justnik.mosplace.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.Resource
import com.justnik.mosplace.data.network.authmodels.LoginInfo
import com.justnik.mosplace.data.prefs.SettingsPrefs
import com.justnik.mosplace.presentation.helpers.UiText
import com.justnik.mosplace.domain.usecases.auth.LoginUserUseCase
import com.justnik.mosplace.domain.usecases.auth.ValidateEmail
import com.justnik.mosplace.domain.usecases.auth.ValidatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val loginUserUseCase: LoginUserUseCase,
    private val settingsPrefs: SettingsPrefs
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

                    validationEventChannel.send(ValidationEvent.Error(message))
                }
            }
        }
    }

    fun getDarkModeCode() = settingsPrefs.darkModeCode

    sealed class ValidationEvent {
        class Success(val successMessage: UiText) : ValidationEvent()
        class Error(val errorMessage: UiText) : ValidationEvent()
    }

    data class UiState(
        val isLoading: Boolean = false
    )
}
