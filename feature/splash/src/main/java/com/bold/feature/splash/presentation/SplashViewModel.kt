package com.bold.feature.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bold.feature.splash.domain.usecase.InitializeAppUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val initializeAppUseCase: InitializeAppUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect: SharedFlow<SplashEffect> = _effect.asSharedFlow()

    init {
        handleIntent(SplashIntent.LoadApp)
    }

    fun handleIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.LoadApp -> initializeApp()
        }
    }

    private fun initializeApp() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            initializeAppUseCase()
            _state.value = _state.value.copy(isLoading = false)
            _effect.emit(SplashEffect.NavigateToHome)
        }
    }
}
