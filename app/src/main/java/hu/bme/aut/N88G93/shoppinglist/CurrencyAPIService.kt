package hu.bme.aut.N88G93.shoppinglist

import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest")
    suspend fun getExchangeRates(
        @Query("base") baseCurrency: String): CurrencyData
}