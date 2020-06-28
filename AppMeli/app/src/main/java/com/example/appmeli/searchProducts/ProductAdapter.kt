package com.example.appmeli.searchProducts
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appmeli.R
import com.example.appmeli.databinding.ListItemProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter : RecyclerView.Adapter<ProductViewHolder>(){

    private val productList = mutableListOf<Article>()
    private var productClickListener : ProductClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val productView = ListItemProductBinding.inflate( LayoutInflater.from(parent.context),  parent, false)
        return ProductViewHolder(productView)
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.title.text = product.title
        holder.binding.price.text = "$ " + product.price
        holder.binding.legend.text = "Condición: " + if(product.condition == "new") "Nuevo" else "Usado"
        holder.binding.description.text =  product.id

        Picasso.get()
            .load(product.imageUrl)
            .error(R.drawable.ic_android_error)
            .into(holder.binding.productImage)

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

class  ProductViewHolder( val binding: ListItemProductBinding): RecyclerView.ViewHolder(binding.root)

interface ProductClickListener{
    fun onProductClick(article: Article){}
}