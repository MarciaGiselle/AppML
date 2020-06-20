package com.example.appmeli.searchProducts
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MercadoLibreAPI {
    @GET("items/{itemId}")
    fun getArticle(@Path("itemId") id: String): Call<ArticleResponse>

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


data class ArticleResponse(
    @SerializedName(value="title")
    val title : String,
    @SerializedName(value="subtitle")
    val subtitle: String?,
    @SerializedName(value = "id")
    val id: String,
    @SerializedName(value = "category_id")
    val categoryId: String,
    @SerializedName(value="name")
    val categoryName : String,
    @SerializedName(value = "price")
    val price: String,
    @SerializedName(value = "available_quantity")
    val quantity: String,
    @SerializedName(value = "sold_quantity")
    val quantitySold: String,
    @SerializedName(value = "condition")
    val condition: String,
    @SerializedName(value = "thumbnail")
    val imageUrl : String,
    @SerializedName(value = "accepts_mercadopago")
    val mercadoPago: Boolean,
    @SerializedName(value = "warranty")
    val warranty: String

)