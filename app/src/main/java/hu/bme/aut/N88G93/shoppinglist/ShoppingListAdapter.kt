import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.N88G93.shoppinglist.DetailActivity
import hu.bme.aut.N88G93.shoppinglist.Item
import hu.bme.aut.N88G93.shoppinglist.ItemEntity
import hu.bme.aut.N88G93.shoppinglist.MainActivity
import hu.bme.aut.N88G93.shoppinglist.NewItem
import hu.bme.aut.N88G93.shoppinglist.NewItemViewModel
import hu.bme.aut.N88G93.shoppinglist.R

class ShoppingListAdapter(val listener: ShoppingListAdapter.RowClickListener) :
    RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>() {
    var items  = ArrayList<ItemEntity>()

    fun setListData(data: List<ItemEntity>) {
        this.items = ArrayList(data)
        notifyDataSetChanged()
    }
    inner class ViewHolder(itemView: View,val listener: RowClickListener) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.image)
        val itemName: TextView = itemView.findViewById(R.id.name)
        val itemDescription: Button = itemView.findViewById(R.id.Description)
        val estimatedPrice: TextView = itemView.findViewById(R.id.price)
        val isPurchased: CheckBox = itemView.findViewById(R.id.isPurhcased)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
        val EditButton: Button = itemView.findViewById(R.id.Edit)
       // val category:TextView=itemView.findViewById(R.id.spinnerCategory)
        //private lateinit var ViewModel : NewItemViewModel

        init {
            EditButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    val item = items[position]
                    listener.onEditClickListener(item,getCategoryNameFromIcon(getCategoryIcon(item.category)))
                }
            }
            deleteButton.setOnClickListener {
                Toast.makeText(itemView.context, "Item deleted", Toast.LENGTH_SHORT).show()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    listener.onDeleteUserClickListener(item)
                }
            }
            itemDescription.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    val item = items[position]
                    listener.onItemClickListener(item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set the category icon based on the item's category
        holder.categoryIcon.setImageResource(getCategoryIcon(item.category))

        // Set other item details
        holder.itemName.text = item.name
        holder.estimatedPrice.text = "Price: ${item.estimatedPriceHUF} HUF"

        // Set the CheckBox for marking if the item is bought
        holder.isPurchased.isChecked= item.isBought
        holder.isPurchased.setOnCheckedChangeListener { _, isChecked ->
            item.isBought = isChecked
        }
        //holder.category.text=item.category

    }
    fun getCategoryIcon(category: String): Int {
        return when (category) {
            "Food" -> R.drawable.food
            "Electronic" -> R.drawable.electronic
            "Stationery" -> R.drawable.stationery
            "Clothes"->R.drawable.clothes
            "Beauty"->R.drawable.beauty
            "Toys"->R.drawable.toys
            "Sports"->R.drawable.sports
            "Furniture"->R.drawable.furniture
            "Home Appliance"->R.drawable.home_applicance// Add more cases for other categories as needed
            else -> R.drawable.app_logo
        }
    }
    fun getCategoryNameFromIcon(iconResId: Int): String {
        return when (iconResId) {
            R.drawable.food -> "Food"
            R.drawable.electronic -> "Electronic"
            R.drawable.stationery -> "Stationery"
            R.drawable.clothes -> "Clothes"
            R.drawable.beauty -> "Beauty"
            R.drawable.toys -> "Toys"
            R.drawable.sports -> "Sports"
            R.drawable.furniture -> "Furniture"
            R.drawable.home_applicance-> "Home Appliance"
            else -> "Category"
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun setItems(newItems: List<ItemEntity>) {
        val items = newItems
        notifyDataSetChanged()
    }
    interface RowClickListener{
        fun onDeleteUserClickListener(item: ItemEntity)
        fun onItemClickListener(item: ItemEntity)
        fun onEditClickListener(item: ItemEntity,categoryName: String)
    }
}
