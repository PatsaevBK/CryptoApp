package com.example.cryptoapp.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.CryptoApp
import com.example.cryptoapp.databinding.FragmentCoinDetailBinding
import com.example.cryptoapp.domain.entities.CoinInfo
import com.squareup.picasso.Picasso
import javax.inject.Inject


class CoinDetailFragment : Fragment() {

    private lateinit var fromSymbol: String

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding
        get() = _binding!!

    private val component by lazy {
        (requireActivity().application as CryptoApp).applicationComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[CoinViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            fromSymbol = it.getString(ARG_PARAM1) ?: EMPTY_SYMBOL
        }
        Log.d("CoinDetailFragment", "$viewModel")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getDetailInfo(fromSymbol).observe(viewLifecycleOwner) {
            with(binding) {
                tvPrice.text = it.price
                tvMinPrice.text = it.lowDay
                tvMaxPrice.text = it.highDay
                tvLastMarket.text = it.lastMarket
                tvLastUpdate.text = it.lastUpdate
                tvFromSymbol.text = it.fromSymbol
                tvToSymbol.text = it.toSymbol
                Picasso.get().load(it.imageUrl).into(ivLogoCoin)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PARAM1 = "coinInfo"
        private const val EMPTY_SYMBOL = ""

        @JvmStatic
        fun newInstance(fromSymbol: String) =
            CoinDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, fromSymbol)
                }
            }
    }
}