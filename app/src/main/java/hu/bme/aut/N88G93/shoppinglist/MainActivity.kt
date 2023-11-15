package hu.bme.aut.N88G93.shoppinglist

import ShoppingListAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(),ShoppingListAdapter.RowClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ShoppingListAdapter
    private val itemList = mutableListOf<Item>()
    private val ADD_ITEM_REQUEST_CODE = 1
    private lateinit var ViewModel : NewItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewModel= ViewModelProvider(this).get(NewItemViewModel::class.java)
        recyclerView = findViewById(R.id.recyclerView)
        itemAdapter = ShoppingListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemAdapter
        ViewModel.allItems.observe(this, Observer { items ->
            itemAdapter.setItems(items)
        })
//
        ViewModel.allItems.observe(this, Observer { itemEntities ->
            // Convert ItemEntity objects to Item objects using toDomainModel()
            val items = itemEntities.map { it.toDomainModel() }

            // Update the itemList and notify the adapter
            itemAdapter.setListData(itemEntities)
            itemAdapter.notifyDataSetChanged()
            val addItemButton = findViewById<Button>(R.id.addItemButton)
            val imageView = findViewById<ImageView>(R.id.imageView)

            if (items.isEmpty()) {
                addItemButton.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                addItemButton.setOnClickListener {
                    val intent = Intent(this, NewItem::class.java)
                    startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
                }
            } else {
                addItemButton.visibility = View.GONE
                imageView.visibility = View.GONE

            }

        })


    }
    private fun onDeleteItemClick(item: ItemEntity) {
        // Call the deleteItem function in the ViewModel to delete the item from the database
        ViewModel.deleteItem(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addItem -> {
                val intent = Intent(this, NewItem::class.java)

                startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)

                return true
            }
            R.id.deleteItem -> {
                // Handle item 2 click
                showDeleteAllConfirmationDialog()

                return true

            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    private fun showDeleteAllConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete All Items")
        builder.setMessage("Are you sure you want to delete all items?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User clicked Yes, delete all items
            ViewModel.deleteAllItems()
            Toast.makeText(this, "All items deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User clicked No, dismiss the dialog
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the data from the intent
            val itemName = data?.getStringExtra("item_name")
            val category = data?.getStringExtra("category")
            val estimatedPrice = data?.getFloatExtra("estimated_price", 0f)
            val description = data?.getStringExtra("description")
            val isBought = data?.getBooleanExtra("is_bought", false)

            // Create a new ItemModel
            val newItem = Item(itemName.orEmpty(), category.orEmpty(), estimatedPrice ?: 0f, description.orEmpty(),isBought ?: false)

            // Add the new item to the list and notify the adapter
            itemList.add(newItem)
            itemAdapter.notifyDataSetChanged()
        }
    }

   override fun onDeleteUserClickListener(item: ItemEntity) {
       // Handle the delete button click here
       ViewModel.deleteItem(item)
   }

    override fun onItemClickListener(item: ItemEntity) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("ITEM_NAME", item.name)
        intent.putExtra("ITEM_DESCRIPTION", item.description)
        intent.putExtra("ITEM_ESTIMATED_PRICE", item.estimatedPriceHUF)
        intent.putExtra("ITEM_IS_BOUGHT", item.isBought)

        startActivity(intent)
    }

    override fun onEditClickListener(item: ItemEntity,categoryName:String) {
        val intent = Intent(this, NewItem::class.java)

        intent.putExtra("ITEM_ID", item.id)
        intent.putExtra("ITEM_NAME", item.name)
        intent.putExtra("ITEM_DESCRIPTION", item.description)
        intent.putExtra("ITEM_ESTIMATED_PRICE", item.estimatedPriceHUF)
        intent.putExtra("ITEM_IS_BOUGHT", item.isBought)
        intent.putExtra("ITEM_CATEGORY",categoryName)

        startActivity(intent)
    }


}