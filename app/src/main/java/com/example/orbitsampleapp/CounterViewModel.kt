package com.example.orbitsampleapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CounterViewModel : ViewModel() {

    private val actionChannel = Channel<CounterAction>()
    val state = actionChannel.receiveAsFlow()
        .map(::reduce)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CounterState()
        )

    private val _sideEffect = Channel<CounterSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun increase() {
        viewModelScope.launch {
            if (state.value.count < 10) {
                dispatch(CounterAction.Increment)
            } else {
                postSideEffect(CounterSideEffect.Toast("10 이하여야 합니다."))
            }
        }
    }

    fun decrease() {
        viewModelScope.launch {
            if (state.value.count > 0) {
                dispatch(CounterAction.Decrement)
            } else {
                postSideEffect(CounterSideEffect.Toast("0 이상이어야 합니다."))
            }
        }
    }

    private suspend fun dispatch(event: CounterAction) {
        actionChannel.send(event)
    }

    private fun reduce(action: CounterAction): CounterState {
        return when (action) {
            is CounterAction.Increment -> {
                state.value.copy(count = state.value.count + 1)
            }
            is CounterAction.Decrement -> {
                state.value.copy(count = state.value.count - 1)
            }
        }
    }

    private suspend fun postSideEffect(sideEffect: CounterSideEffect) {
        _sideEffect.send(sideEffect)
    }
}

sealed class CounterAction {
    object Increment : CounterAction()
    object Decrement : CounterAction()
}
