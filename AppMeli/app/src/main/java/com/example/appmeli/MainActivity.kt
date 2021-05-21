package com.example.appmeli
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmeli.databinding.ActivityMainBinding
import com.example.appmeli.searchProducts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var productService : RetrofitProductService
    private lateinit var productAdapter : ProductAdapter
    private lateinit var binding : ActivityMainBinding
    private lateinit  var toast : Toast

    private fun injectDependencies(){
        productService = RetrofitProductService()
        productAdapter = ProductAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        injectDependencies()
        setSearchListener()
        setUpRecyclerView()
      if( ! isOnline(this)){
        toast = Toast.makeText(this@MainActivity, R.string.sinConexion, Toast.LENGTH_LONG)
          toast.setGravity(Gravity.CENTER,0,0)
          toast.show()
      }
    }

    private fun setUpRecyclerView(){
        binding.recyclerArticles.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerArticles.adapter = productAdapter
        productAdapter.setProductClickListener(object : ProductClickListener {
            override fun onProductClick(article: Article) {
                 val intent = Intent(this@MainActivity, ProductActivity::class.java)
                intent.putExtra("product_id", article.id)
                startActivity(intent)
            }

        })
    }

    private fun setSearchListener(){
       binding.searchInput.setOnQueryTextListener(
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

    private fun onSearch(query: String?){
        query?.run {
            binding.bienvenido.visibility = View.GONE
            binding.progressCircular.visibility = View.VISIBLE
                productService.search(query)
                    .enqueue(object :Callback<SearchResult> {
                        override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                            binding.progressCircular.visibility = View.INVISIBLE
                            toast =  Toast.makeText(this@MainActivity, R.string.notNetwork, Toast.LENGTH_LONG)
                                toast.setGravity(Gravity.CENTER,0,0)
                                toast.show()
                        }
                        override fun onResponse(
                            call: Call<SearchResult>,
                            response: Response<SearchResult>
                        ) {
                            if(response.isSuccessful ){
                                val articles = response.body()!!
                                binding.progressCircular.visibility = View.INVISIBLE

                                if(articles.results.isEmpty()){
                                    toast = Toast.makeText(this@MainActivity, "No hay resultados, intenta nuevamente.", Toast.LENGTH_LONG)
                                    toast.setGravity(Gravity.CENTER,0,0)
                                    toast.show()
                                }
                                else{
                                    productAdapter.updateArticles( articles.results)
                                    productAdapter.notifyDataSetChanged()
                                }
                            }else{
                                Toast.makeText(this@MainActivity, "Error interno", Toast.LENGTH_LONG).show()

                            }
                        }
                    })
            }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        } else {
        val activeNetworkInfo = connectivityManager.activeNetwork
            if (activeNetworkInfo != null) {
                return true
            }
        }
        return false
    }
}