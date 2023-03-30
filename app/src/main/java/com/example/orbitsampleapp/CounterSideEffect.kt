package com.example.orbitsampleapp

sealed class CounterSideEffect {
    data class Toast(val text: String) : CounterSideEffect()
}