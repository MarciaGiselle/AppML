package com.example.appmeli

import android.content.Intent
import android.content.LocusId
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeli.SearchProducts.*
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var productService : RetrofitProductService
    private lateinit var productAdapter: ProductAdapter

    private fun injectDependencies(){
        productService = RetrofitProductService()
        productAdapter = ProductAdapter()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependencies()
        setSearchListener()
        setUpRecyclerView()



        val imageCarrousel1 = findViewById<ImageView>(R.id.imageCarrousel1)
        val imageCarrousel2 = findViewById<ImageView>(R.id.imageCarrousel2)

        Picasso.get()
            .load(R.drawable.ic_bannermeli_tarjeta)
            .error(R.drawable.ic_android_error)
            .into(imageCarrousel1)

        Picasso.get()
            .load(R.drawable.ic_bannermeli_dos)
            .error(R.drawable.ic_android_error)
            .into(imageCarrousel2)
    }

    private fun setUpRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerArticles)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = productAdapter
        productAdapter.setProductClickListener(object : ProductClickListener {
            override fun onProductClick(article: Article) {
                Toast.makeText(this@MainActivity, article.title, Toast.LENGTH_LONG).show()
                 val intent = Intent(this@MainActivity, ProductActivity::class.java)
                startActivity(intent)
            }

        })
    }

    private fun setSearchListener(){
        findViewById<SearchView>(R.id.searchInput).setOnQueryTextListener(
            object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                  onSearch(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                   return false
                }
            }
        )
    }

    private fun onSearch(id: String?){
            id?.run {
                productService.search(id)
                    .enqueue(object :Callback<SearchResult> {
                        override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(
                            call: Call<SearchResult>,
                            response: Response<SearchResult>
                        ) {
                            if(response.isSuccessful){
                                val articles = response.body()!!
                                productAdapter.updateArticles( articles.results)
                                productAdapter.notifyDataSetChanged()
                            }else{
                                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG)

                            }
                        }
                    })
            }
    }

}

