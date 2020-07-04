package com.example.appmeli

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
            binding.progressCircular.visibility = View.VISIBLE
            productService.getArticle(query)
                .enqueue(object : Callback<ArticleResponse> {
                    override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                        binding.progressCircular.visibility = View.INVISIBLE
                        Toast.makeText(this@ProductActivity, R.string.notNetwork, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<ArticleResponse>,
                        response: Response<ArticleResponse>
                    ) {
                        if(response.isSuccessful){
                            binding.progressCircular.visibility = View.INVISIBLE
                            setResultProduct(response.body()!!)
                        }else{
                            Toast.makeText(this@ProductActivity, R.string.error, Toast.LENGTH_LONG).show()

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
        binding.warranty.text = if (body.warranty != null) body.warranty else getString(R.string.garantia)
        binding.condicioncantidad.text =
            getString(R.string.state, if (body.condition == "new") getString(R.string.nuevo) else getString(R.string.usado), body.quantitySold )

        binding.mercadoPago.text = if (body.mercadoPago) getString(R.string.mercadoPago) else getString(R.string.cuotas)
        binding.categoria.text = getString(R.string.category, body.categoryId)

        binding.legend.text = getString(R.string.devolucion)

        Picasso.get()
            .load(body.imageUrl)
            .placeholder(R.drawable.ic_gallery_icon_vector_26537164)
            .error(R.drawable.ic_android_error)
            .into(binding.imageProduct)
    }
}
