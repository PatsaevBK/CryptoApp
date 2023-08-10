package com.example.cryptoapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.CryptoApp
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityCoinPrceListBinding
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.presentation.adapters.CoinInfoAdapter
import javax.inject.Inject

class CoinPriceListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCoinPrceListBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CoinViewModel::class.java]
    }

    private val applicationComponent by lazy {
        (application as CryptoApp).applicationComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTopAppBar()
        setupRecycleView()
        Log.d("CoinPriceListActivity", "$viewModel")
    }

    private fun setupTopAppBar() {
        with(binding.topAppBar) {
            this?.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.sortLastUpdate -> {
                        makeSort(menuItem, CoinViewModel.FILTER_LAST_UPDATE)
                        true
                    }
                    R.id.sortPrice -> {
                        makeSort(menuItem, CoinViewModel.FILTER_PRICE)
                        true
                    }
                    R.id.sortAz -> {
                        makeSort(menuItem, CoinViewModel.FILTER_A_Z)
                        true
                    }
                    else -> false
                }
            }
            this?.menu?.get(0)?.let {
                it.isChecked = true
                makeSort(it, CoinViewModel.FILTER_LAST_UPDATE)
            }

        }
    }

    private fun makeSort(menuItem: MenuItem, constSort: String) {
        menuItem.isChecked = !menuItem.isChecked
        viewModel.makeSort(constSort)
    }

    private fun setupRecycleView() {
        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener = if (isOnePaneMode()) {
            {
                launchActivity(it)
            }
        } else {
            {
                launchFragment(CoinDetailFragment.newInstance(it.fromSymbol))
            }
        }
        binding.rvCoinPriceList.adapter = adapter
        viewModel.coinInfoList.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun launchActivity(it: CoinInfo) {
        val intent = CoinDetailActivity.newIntent(
            this@CoinPriceListActivity,
            it.fromSymbol
        )
        startActivity(intent)
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
