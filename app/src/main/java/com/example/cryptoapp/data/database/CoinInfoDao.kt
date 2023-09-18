package com.example.cryptoapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CoinInfoDao {
    @Query("SELECT * FROM full_price_list ORDER BY lastUpdate DESC")
    fun getPriceList(): Flow<List<CoinInfoDbModel>>

    @Query("SELECT * FROM full_price_list WHERE fromSymbol == :fSym LIMIT 1")
    fun getPriceInfoAboutCoin(fSym: String): Flow<CoinInfoDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceList(priceList: List<CoinInfoDbModel>)

    @Query("SELECT * FROM full_price_list ORDER BY fromSymbol")
    fun getPriceListAZ(): Flow<List<CoinInfoDbModel>>

    @Query("SELECT * FROM full_price_list ORDER BY price DESC")
    fun getPriceListPrice(): Flow<List<CoinInfoDbModel>>

    @Query("SELECT * FROM full_price_list WHERE fromSymbol LIKE '%' || :fSym || '%'")
    fun searchCoin(fSym: String): Flow<List<CoinInfoDbModel>>
}
