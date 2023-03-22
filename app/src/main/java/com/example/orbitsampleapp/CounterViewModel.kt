package com.example.orbitsampleapp

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class CounterViewModel : ContainerHost<CounterState, CounterSideEffect>, ViewModel() {
    override val container = container<CounterState, CounterSideEffect>(CounterState())

    fun increase() = intent {
        if (state.value < 10) {
            reduce {
                state.copy(value = state.value + 1)
            }
        } else {
            postSideEffect(CounterSideEffect.Toast("10 이하여야 합니다."))
        }
    }

    fun decrease() = intent {
        if (state.value > 0) {
            reduce {
                state.copy(value = state.value - 1)
            }
        } else {
            postSideEffect(CounterSideEffect.Toast("0 이상이어야 합니다."))
        }
    }
}

data class CounterState(
    val value: Int = 0
)

sealed class CounterSideEffect {
    data class Toast(val text: String) : CounterSideEffect()
}