package com.justnik.mosplace.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.network.authmodels.UserInfo
import com.justnik.mosplace.data.repositories.Resource
import com.justnik.mosplace.domain.UiText
import com.justnik.mosplace.domain.usecases.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val validateEmail: ValidateEmail,
    private val validateUsername: ValidateUsername,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
) : ViewModel() {

    private var _registrationFormState = MutableStateFlow(RegistrationFormState())
    val registrationFormState = _registrationFormState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.EmailChanged -> {
                _registrationFormState.value =
                    registrationFormState.value.copy(email = event.email, emailError = null)
            }
            is RegistrationFormEvent.UsernameChanged -> {
                _registrationFormState.value =
                    registrationFormState.value.copy(
                        username = event.username,
                        usernameError = null
                    )
            }
            is RegistrationFormEvent.PasswordChanged -> {
                _registrationFormState.value =
                    registrationFormState.value.copy(
                        password = event.password,
                        passwordError = null
                    )
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                _registrationFormState.value =
                    registrationFormState.value.copy(
                        repeatedPassword = event.repeatedPassword,
                        repeatedPasswordError = null
                    )
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val emailResult = validateEmail(registrationFormState.value.email)
        val usernameResult = validateUsername(registrationFormState.value.username)
        val passwordResult = validatePassword(registrationFormState.value.password)
        val repeatedPasswordResult = validateRepeatedPassword(
            registrationFormState.value.password,
            registrationFormState.value.repeatedPassword,
        )

        val hasError = listOf(
            emailResult,
            usernameResult,
            passwordResult,
            repeatedPasswordResult
        ).any {
            !it.successful
        }

        if (hasError) {
            _registrationFormState.value = registrationFormState.value.copy(
                emailError = emailResult.errorMessage,
                usernameError = usernameResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            val email = registrationFormState.value.email
            val username = registrationFormState.value.username
            val password = registrationFormState.value.password
            val userFullInfo = UserInfo(email, password, username)
            val response = createUserUseCase(userFullInfo)
            when (response) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success(UiText.StringResource(R.string.confirm_email)))
                }
                is Resource.Error -> {
                    val message = response.message
                    if (message != null){
                        validationEventChannel.send(ValidationEvent.Error(message))
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

}
