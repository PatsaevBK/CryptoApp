package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.models.CoinInfoDto
import com.example.cryptoapp.data.network.models.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.models.CoinNamesListDto
import com.example.cryptoapp.domain.entities.CoinInfo
import com.google.gson.Gson

class CoinMapper {

    fun mapDtoToDbModel(coinInfoDto: CoinInfoDto): CoinInfoDbModel = CoinInfoDbModel(
        coinInfoDto.fromSymbol,
        coinInfoDto.toSymbol,
        coinInfoDto.price,
        coinInfoDto.lastUpdate,
        coinInfoDto.highDay,
        coinInfoDto.lowDay,
        coinInfoDto.lastMarket,
        coinInfoDto.imageUrl
    )

    fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(namesListDto: CoinNamesListDto): String = namesListDto.names?.map {
        it.coinNameDto?.name
    }?.joinToString(",") ?: ""

    fun mapCoinInfoDbModelToCoinInfo(coinInfoDbModel: CoinInfoDbModel) = CoinInfo(
        coinInfoDbModel.fromSymbol,
        coinInfoDbModel.toSymbol,
        coinInfoDbModel.price,
        coinInfoDbModel.lastUpdate,
        coinInfoDbModel.highDay,
        coinInfoDbModel.lowDay,
        coinInfoDbModel.lastMarket,
        coinInfoDbModel.imageUrl
    )
}