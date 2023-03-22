package com.example.orbitsampleapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.orbitsampleapp.databinding.ActivityCounterBinding
import org.orbitmvi.orbit.viewmodel.observe

class CounterActivity : AppCompatActivity() {
    private val viewModel by viewModels<CounterViewModel>()
    private val binding by lazy { ActivityCounterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        viewModel.observe(lifecycleOwner = this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun initViews() = with(binding) {
        btnIncrease.setOnClickListener { viewModel.increase() }
        btnDecrease.setOnClickListener { viewModel.decrease() }
    }

    private fun render(state: CounterState) = with(binding) {
        tvNumber.text = state.value.toString()
    }

    private fun handleSideEffect(sideEffect: CounterSideEffect) {
        when (sideEffect) {
            is CounterSideEffect.Toast -> {
                Toast.makeText(this, sideEffect.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}