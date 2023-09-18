package com.example.cryptoapp.presentation

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.CryptoApp
import com.example.cryptoapp.R
import com.example.cryptoapp.databinding.ActivityCoinPrceListBinding
import com.example.cryptoapp.domain.entities.CoinInfo
import com.example.cryptoapp.presentation.adapters.CoinInfoAdapter
import com.example.cryptoapp.presentation.viewmodel.CoinViewModel
import com.example.cryptoapp.presentation.viewmodel.ViewModelFactory
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

    private var selectedItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        applicationComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTopAppBar()
        setupRecycleView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            menuInflater.inflate(R.menu.menu, menu)
            val searchItem = menu.findItem(R.id.actionSearch)
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint = getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.makeSearch(newText!!)
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupTopAppBar() {
        with(binding) {
            setSupportActionBar(topAppBar)
            topAppBar?.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.actionSort -> {
                        filter()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun filter() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.choose_sort)
            .setSingleChoiceItems(R.array.sortItems, selectedItem) { dialog, item ->
                selectItem(item)
                selectedItem = item
                binding.rvCoinPriceList.smoothScrollToPosition(0)
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun selectItem(item: Int) {
        when (item) {
            0 -> viewModel.makeSortLastUpdate()
            1 -> viewModel.makeSortAZ()
            2 -> viewModel.makeSortPrice()
        }
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
