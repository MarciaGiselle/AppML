package com.example.appmeli

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmeli.searchProducts.ArticleResponse
import com.example.appmeli.searchProducts.RetrofitProductService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var productService : RetrofitProductService

    private fun injectDependencies() {
        productService = RetrofitProductService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_product)

        val id : String? = intent.getStringExtra("product_id")

        getArticle(id)
    }

    private fun getArticle(query: String?){
        query?.run {
            productService.getArticle(query)
                .enqueue(object : Callback<ArticleResponse> {
                    override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                        Toast.makeText(this@ProductActivity, "Error : ${t.message}", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<ArticleResponse>,
                        response: Response<ArticleResponse>
                    ) {
                        if(response.isSuccessful){
                            setResultProduct(response.body()!!)
                        }else{
                            Toast.makeText(this@ProductActivity, "Error", Toast.LENGTH_LONG).show()

                        }
                    }
                })
        }
    }


    private fun setResultProduct(body: ArticleResponse) {

        var titleProduct= findViewById<TextView>(R.id.titleProduct)
        var subtitle = findViewById<TextView>(R.id.subtitle)
        var price = findViewById<TextView>(R.id.priceProduct)
        var quantity = findViewById<TextView>(R.id.cantidadDisponible)
        var warranty =findViewById<TextView>(R.id.warranty)
        var condition = findViewById<TextView>(R.id.condicioncantidad)
        var mercadoPago = findViewById<TextView>(R.id.cuotas)
        var categoria = findViewById<TextView>(R.id.categoria)
        var imageProduct = findViewById<ImageView>(R.id.imageProduct)

        titleProduct.text = body.title
        subtitle.text =  if (body.subtitle != null) body.subtitle else body.id
        price.text = "$ " + body.price
        quantity.text = "Cantidad disponible : " + body.quantity
        warranty.text = if (body.warranty !=null ) body.warranty else "Sin garantía"
        condition.text =
            """${if (body.condition == "new") "Nuevo" else "Usado"} - ${body.quantitySold} vendidos"""
        mercadoPago.text = if (body.mercadoPago) "Acepta Mercado Pago" else "Cuotas sin interés"
        categoria.text = "Categoría : " + body.categoryId

        Picasso.get()
            .load(body.imageUrl)
            .error(R.drawable.ic_android_error)
            .into(imageProduct)
    }
}
