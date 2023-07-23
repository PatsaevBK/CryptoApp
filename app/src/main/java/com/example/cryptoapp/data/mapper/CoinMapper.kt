package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.models.CoinInfoDto
import com.example.cryptoapp.data.network.models.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.models.CoinNamesListDto
import com.example.cryptoapp.domain.entities.CoinInfo
import com.google.gson.Gson
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class CoinMapper {

    fun mapDtoToDbModel(coinInfoDto: CoinInfoDto): CoinInfoDbModel = CoinInfoDbModel(
        coinInfoDto.fromSymbol,
        coinInfoDto.toSymbol,
        coinInfoDto.price,
        coinInfoDto.lastUpdate,
        coinInfoDto.highDay,
        coinInfoDto.lowDay,
        coinInfoDto.lastMarket,
        BASE_IMAGE_URL + coinInfoDto.imageUrl
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
        convertTimestampToTime(coinInfoDbModel.lastUpdate),
        coinInfoDbModel.highDay,
        coinInfoDbModel.lowDay,
        coinInfoDbModel.lastMarket,
        coinInfoDbModel.imageUrl
    )

    private fun convertTimestampToTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "HH:mm:ss"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    companion object {
        const val BASE_IMAGE_URL = "https://cryptocompare.com"
    }
}