package com.example.appmeli

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmeli.databinding.ActivityProductBinding
import com.example.appmeli.searchProducts.ArticleResponse
import com.example.appmeli.searchProducts.RetrofitProductService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var productService : RetrofitProductService
    private lateinit var binding: ActivityProductBinding

    private fun injectDependencies() {
        productService = RetrofitProductService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        binding = ActivityProductBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

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

        binding.titleProduct.text = body.title
        binding.subtitle.text =  if (body.subtitle != null) body.subtitle else body.id
        binding.priceProduct.text = "$ " + body.price
        binding.quantity.text = "Cantidad disponible : " + body.quantity
        binding.warranty.text = if (body.warranty !=null ) body.warranty else "Sin garantía"
        binding.condicioncantidad.text =
            """${if (body.condition == "new") "Nuevo" else "Usado"} - ${body.quantitySold} vendidos"""
        binding.mercadoPago.text = if (body.mercadoPago) "Acepta Mercado Pago" else "Cuotas sin interés"
        binding.categoria.text = "Categoría : " + body.categoryId

        Picasso.get()
            .load(body.imageUrl)
            .error(R.drawable.ic_android_error)
            .into(binding.imageProduct)
    }
}
