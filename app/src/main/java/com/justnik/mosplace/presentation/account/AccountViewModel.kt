package com.justnik.mosplace.presentation.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justnik.mosplace.R
import com.justnik.mosplace.data.prefs.UserPrefs
import com.justnik.mosplace.helpers.network.Resource
import com.justnik.mosplace.data.prefs.SettingsPrefs
import com.justnik.mosplace.helpers.ui.UiText
import com.justnik.mosplace.domain.entities.profile.Profile
import com.justnik.mosplace.domain.usecases.auth.LoadUserUseCase
import com.justnik.mosplace.domain.usecases.profile.LoadProfileUseCase
import com.justnik.mosplace.helpers.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserUseCase: LoadUserUseCase,
    private val getProfileUseCase: LoadProfileUseCase,
    private val userPrefs: UserPrefs,
    private val settingsPrefs: SettingsPrefs
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState<Profile>())
    val uiState = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun isAuthorized(): Boolean = userPrefs.jwtAccessToken != null

    fun logout(){
        userPrefs.jwtAccessToken = null
        userPrefs.jwtRefreshToken = null
    }

    private fun loadUser() {
        viewModelScope.launch {
            val accessToken = userPrefs.jwtAccessToken ?: return@launch
            val response = getUserUseCase(accessToken)

            when (response) {
                is Resource.Success -> {
                    loadProfile(accessToken, response.data!!.id)
                }
                is Resource.Error -> {

                }
            }
        }
    }

    private fun loadProfile(accessToken: String, id: Long) {
        viewModelScope.launch {
            val response = getProfileUseCase(accessToken, id)
            when (response) {
                is Resource.Success -> {
                    _uiState.value = UiState(data = response.data)
                }
                is Resource.Error -> {
                }
            }
        }
    }

    fun getDarkModeCode() = settingsPrefs.darkModeCode
}