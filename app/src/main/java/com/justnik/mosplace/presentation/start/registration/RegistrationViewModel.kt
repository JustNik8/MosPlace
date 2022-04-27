package com.justnik.mosplace.presentation.start.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.data.network.authmodel.UserFullInfo
import com.justnik.mosplace.data.repository.Resource
import com.justnik.mosplace.domain.usecases.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val validateEmail: ValidateEmail,
    private val validateUsername: ValidateUsername,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword
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
            val username = registrationFormState.value.username
            val password = registrationFormState.value.password
            val email = registrationFormState.value.email
            val userFullInfo = UserFullInfo(username, password, email)
            val response = createUserUseCase(userFullInfo)
            when (response) {
                is Resource.Success -> {
                    validationEventChannel.send(ValidationEvent.Success("Confirm email"))
                }
                is Resource.Error -> {
                    if (response.errorCode == 400){
                        try {
                            val jsonArr = response.data?.getJSONArray("password")
                            jsonArr?.let { arr ->
                                val message = Array(arr.length()) {
                                    arr[it]
                                }.joinToString().replace(", ", "\n")
                                validationEventChannel.send(ValidationEvent.Error(message))
                            }
                        } catch (e : Exception){
                            validationEventChannel.send(ValidationEvent.Error("Unknown error!"))
                        }
                    }
                    else{
                        validationEventChannel.send(ValidationEvent.Error("Unknown error!"))
                    }
                }
            }
        }
    }

    sealed class ValidationEvent {
        class Success(val successMessage: String) : ValidationEvent()
        class Error(val errorMessage: String) : ValidationEvent()
    }

//    private var _emailErrorState = MutableStateFlow<String?>(null)
//    val emailErrorState = _emailErrorState.asStateFlow()
//
//    private var _usernameErrorState = MutableStateFlow<String?>(null)
//    val usernameErrorState = _usernameErrorState.asStateFlow()
//
//    private var _passErrorState = MutableStateFlow<String?>(null)
//    val passErrorState = _passErrorState.asStateFlow()
//
//    private var _confirmPassErrorState = MutableStateFlow<String?>(null)
//    val confirmPassErrorState = _confirmPassErrorState.asStateFlow()
//
//    private var _passwordsAreSame = MutableStateFlow<String?>(null)
//    val passwordsAreSame = _passwordsAreSame.asStateFlow()
//
//    private var _showAlertDialog = MutableSharedFlow<String?>()
//    val showAlertDialog = _showAlertDialog.asSharedFlow()
//
//    private fun createUser(userFullInfo: UserFullInfo) {
//        viewModelScope.launch {
//            when (val resource = createUserUseCase(userFullInfo)) {
//                is Resource.Success -> {
//                    Log.d("RRR", resource.data.toString())
//                }
//                is Resource.Error -> {
//                    if (resource.errorCode == 400){
//                        val jsonArrResponse = resource.data?.getJSONArray("password")
//                        jsonArrResponse?.let { jsonArr ->
//                            val string = Array(jsonArr.length()){
//                                jsonArr.getString(it)
//                            }.joinToString { "$it\n" }.replace(", ", "")
//
//                            _showAlertDialog.emit(string)
//                            Log.d("RRR", string)
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//
//    fun validateInput(email: String, username: String, pass: String, confirmPass: String) {
//        val isEmailValid = validateEmail(email)
//        val isUsernameValid = validateUsername(username)
//        val isPassValid = validatePass(pass)
//        val isConfirmPassValid = validateConfirmPass(pass, confirmPass)
//
//        if (isEmailValid && isUsernameValid && isPassValid && isConfirmPassValid) {
//            val userFullInfo = UserFullInfo(username, pass, email)
//            createUser(userFullInfo)
//        }
//    }
//
//    private fun validateEmail(email: String): Boolean {
//        var error: String? = null
//        val regex = Regex("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}\$")
//        if (email.isEmpty()) {
//            error = "Empty field"
//        } else if (!regex.matches(email)) {
//            error = "Invalid email"
//        }
//
//        _emailErrorState.value = error
//        return error == null
//    }
//
//    private fun validateUsername(username: String): Boolean {
//        var error: String? = null
//        if (username.isEmpty()) {
//            error = "Empty field"
//        }
//        _usernameErrorState.value = error
//        return error == null
//    }
//
//    private fun validatePass(pass: String): Boolean {
//        var error: String? = null
//        if (pass.isEmpty()) {
//            error = "Empty field"
//        }
//        _passErrorState.value = error
//        return error == null
//    }
//
//    private fun validateConfirmPass(pass: String, confirmPass: String): Boolean {
//        if (confirmPass.isEmpty()) {
//            _confirmPassErrorState.value = "Empty field"
//            return false
//        } else if (confirmPass != pass && pass.isNotEmpty()) {
//            _passwordsAreSame.value = "Passwords are not same"
//            return false
//        }
//        return true
//    }
//
//    fun resetPassError() {
//        _passErrorState.value = null
//    }
//
//    fun resetConfirmPassError() {
//        _confirmPassErrorState.value = null
//    }
//
//    fun resetPasswordsNotSameError() {
//        _passwordsAreSame.value = null
//    }
//
//    fun resetEmailError() {
//        _emailErrorState.value = null
//    }
//
//    fun resetUsernameError() {
//        _usernameErrorState.value = null
//    }
}
