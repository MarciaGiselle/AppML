package com.example.appmeli.SearchProducts

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeli.R
import com.squareup.picasso.Picasso

class ProductAdapter : RecyclerView.Adapter<ProductViewHolder>(){

    private val productList = mutableListOf<Article>()
    private var productClickListener : ProductClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_product, parent, false)
        return ProductViewHolder(productView)
    }

    override fun getItemCount() = productList.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.title.text = product.title
        holder.price.text = "$ " + product.price
        holder.legend.text = "Condición: " + if(product.condition == "new") "Nuevo" else "Usado"
        holder.description.text =  product.id

        Picasso.get()
            .load(product.imageUrl)
            .error(R.drawable.ic_android_error)
            .into(holder.cover)

        holder.itemView.setOnClickListener{
            productClickListener?.onProductClick(product)
        }


    }

    fun updateArticles( results: List<Article>?){
        productList.clear()
        if(results != null) {
            productList.addAll(results)
        }
    }

    fun setProductClickListener (clickListener: ProductClickListener){
        this.productClickListener = clickListener
    }

}

class  ProductViewHolder( view: View): RecyclerView.ViewHolder(view){
    val cover: ImageView = view.findViewById(R.id.product_image)
    val title : TextView = view.findViewById(R.id.title)
    val description : TextView = view.findViewById(R.id.description)
    val price : TextView = view.findViewById(R.id.price)
    val legend : TextView = view.findViewById(R.id.legend)
}

interface ProductClickListener{
    fun onProductClick(article: Article){}
}