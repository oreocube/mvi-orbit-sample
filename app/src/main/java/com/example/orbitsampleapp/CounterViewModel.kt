package com.example.orbitsampleapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {
    private val _state = MutableStateFlow(CounterState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<CounterSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun increase() {
        viewModelScope.launch {
            if (state.value.count < 10) {
                reduce { prev ->
                    prev.copy(count = prev.count + 1)
                }
            } else {
                postSideEffect(CounterSideEffect.Toast("10 이하여야 합니다."))
            }
        }
    }

    fun decrease() {
        viewModelScope.launch {
            if (state.value.count > 0) {
                reduce { prev ->
                    prev.copy(count = prev.count - 1)
                }
            } else {
                postSideEffect(CounterSideEffect.Toast("0 이상이어야 합니다."))
            }
        }
    }

    private fun reduce(reducer: (CounterState) -> CounterState) {
        _state.update(reducer)
    }

    private suspend fun postSideEffect(sideEffect: CounterSideEffect) {
        _sideEffect.send(sideEffect)
    }
}