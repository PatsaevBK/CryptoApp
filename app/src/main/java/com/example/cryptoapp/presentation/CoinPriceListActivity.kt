package com.example.cryptoapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.databinding.ActivityCoinPrceListBinding
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.presentation.adapters.CoinInfoAdapter

class CoinPriceListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCoinPrceListBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CoinViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecycleView()
    }

    private fun setupRecycleView() {
        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = if (isOnePaneMode()) {
            {
                val intent = CoinDetailActivity.newIntent(
                    this@CoinPriceListActivity,
                    it.fromSymbol
                )
                startActivity(intent)
            }
        } else {
            {
                launchFragment(CoinDetailFragment.newInstance(it))
            }
        }
        binding.rvCoinPriceList.adapter = adapter
        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView!!.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun isOnePaneMode() = binding.fragmentContainerView == null
}
