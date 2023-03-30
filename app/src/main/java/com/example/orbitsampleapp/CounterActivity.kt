package com.example.orbitsampleapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.orbitsampleapp.databinding.ActivityCounterBinding
import kotlinx.coroutines.launch

class CounterActivity : AppCompatActivity() {
    private val viewModel by viewModels<CounterViewModel>()
    private val binding by lazy { ActivityCounterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        observeData()
    }

    private fun initViews() = with(binding) {
        btnIncrease.setOnClickListener { viewModel.increase() }
        btnDecrease.setOnClickListener { viewModel.decrease() }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.state.collect(::render) }
                launch { viewModel.sideEffect.collect(::handleSideEffect) }
            }
        }
    }

    private fun render(state: CounterState) = with(binding) {
        tvNumber.text = state.count.toString()
    }

    private fun handleSideEffect(sideEffect: CounterSideEffect) {
        when (sideEffect) {
            is CounterSideEffect.Toast -> {
                Toast.makeText(this, sideEffect.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}