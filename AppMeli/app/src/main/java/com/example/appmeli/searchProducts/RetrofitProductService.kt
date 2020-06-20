package com.example.appmeli.searchProducts

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val API_URL_MELI= "https://api.mercadolibre.com/"

class RetrofitProductService {

    private val service : MercadoLibreAPI = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(API_URL_MELI)
        .build()
        .create(MercadoLibreAPI::class.java)

    fun search (q :String) : Call<SearchResult> {
        return service.search(q)
    }

    fun getArticle(id : String) : Call<ArticleResponse>{
        return service.getArticle(id)
    }
}