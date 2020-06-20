package com.example.appmeli.SearchProducts
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MercadoLibreAPI {
    @GET("items/{itemId}")
    fun getArticle(@Path("itemId") id: String): Call<Article>

    @GET("sites/MLA/search")
    fun search(@Query("q") query: String?): Call<SearchResult>
}

data class Article(
    @SerializedName(value="title")
    val title : String,
    @SerializedName(value = "id")
    val id: String,
    @SerializedName(value = "price")
    val price: String,
    @SerializedName(value = "available_quantity")
    val quantity: String,
    @SerializedName(value = "condition")
    val condition: String,
    @SerializedName(value = "thumbnail")
    val imageUrl : String
)

data class SearchResult(
    @SerializedName(value="results")
    val results : List<Article>


)

