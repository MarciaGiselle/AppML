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
        binding.subtitle.text = body.subtitle ?: body.id
        binding.priceProduct.text = getString(R.string.signoPeso, body.price)
        binding.quantity.text = getString(R.string.quantity, body.quantity)
        binding.warranty.text = body.warranty
        binding.condicioncantidad.text =
            getString(R.string.state, if (body.condition == "new") getString(R.string.nuevo) else getString(R.string.usado), body.quantitySold )

        binding.mercadoPago.text = if (body.mercadoPago) getString(R.string.mercadoPago) else getString(R.string.cuotas)
        binding.categoria.text = getString(R.string.category, body.categoryId)

        Picasso.get()
            .load(body.imageUrl)
            .error(R.drawable.ic_android_error)
            .into(binding.imageProduct)
    }
}
