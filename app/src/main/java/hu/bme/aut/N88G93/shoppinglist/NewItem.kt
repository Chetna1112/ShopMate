package hu.bme.aut.N88G93.shoppinglist

import ShoppingListAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class NewItem : AppCompatActivity(),ShoppingListAdapter.RowClickListener
{
    private lateinit var itemNameEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var estimatedPriceEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var isBoughtCheckBox: CheckBox
    private lateinit var addItemButton: Button
    private lateinit var ViewModel : NewItemViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        // Initialize views
        itemNameEditText = findViewById(R.id.editTextItemName)
        categorySpinner = findViewById(R.id.spinnerCategory)
        estimatedPriceEditText = findViewById(R.id.editTextEstimatedPrice)
        descriptionEditText = findViewById(R.id.editTextDescription)
        isBoughtCheckBox = findViewById(R.id.checkBoxIsBought)
        addItemButton = findViewById(R.id.buttonAddItem)
        ViewModel=ViewModelProvider(this).get(NewItemViewModel::class.java)

        // Initialize the spinner with categories
        val categories = listOf("Category", "Food", "Electronic", "Stationery", "Clothes", "Beauty", "Toys", "Sports", "Furniture", "Home Appliance")

        val spinner = findViewById<Spinner>(R.id.spinnerCategory)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val itemName = intent.getStringExtra("ITEM_NAME")
        val itemDescription = intent.getStringExtra("ITEM_DESCRIPTION")
        val estimatedPrice = intent.getFloatExtra("ITEM_ESTIMATED_PRICE", 0.0f)
        val isBought = intent.getBooleanExtra("ITEM_IS_BOUGHT", false)
        val category = intent.getStringExtra("ITEM_CATEGORY")
        itemNameEditText.setText(itemName)
        estimatedPriceEditText.setText(estimatedPrice.toString())
        descriptionEditText.setText(itemDescription)
        isBoughtCheckBox.setChecked(isBought)
        val categoryIndex = categories.indexOf(category)

        // Set the spinner selection to the index of the existing category
        if (categoryIndex != -1) {
            spinner.setSelection(categoryIndex)
        }
        // Set click listener for the Add Item button
        addItemButton.setOnClickListener {

            addItemToDatabase()
        }
    }

    private fun addItemToDatabase()
    {
        val itemName = itemNameEditText.text.toString()
        val category = categorySpinner.selectedItem.toString()

        val estimatedPrice = estimatedPriceEditText.text.toString().toFloatOrNull() ?: 0f
        val description = descriptionEditText.text.toString()
        val isBought = isBoughtCheckBox.isChecked

        // Check if the item already exists in the database by checking for an item ID in the intent
        val itemId = intent.getLongExtra("ITEM_ID", -1)

        if (itemId != -1L)
        {
            // If the item ID is present, it means we are editing an existing item
            val updatedItem = ItemEntity(
                id = itemId,
                name = itemName,
                category = category,
                estimatedPriceHUF = estimatedPrice,
                description = description,
                isBought = isBought
            )
            ViewModel.updateItem(updatedItem)

        } else
        {
            // If the item ID is not present, it means we are adding a new item
            val newItem = ItemEntity(
                name = itemName,
                category = category,
                estimatedPriceHUF = estimatedPrice,
                description = description,
                isBought = isBought
            )
            ViewModel.addItem(newItem)

        }

        // Close the activity
        finish()
    }


    override fun onDeleteUserClickListener(item: ItemEntity) {
        TODO("Not yet implemented")
    }

    override fun onItemClickListener(item: ItemEntity) {
        TODO("Not yet implemented")
    }

    override fun onEditClickListener(item: ItemEntity,categoryName: String) {

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
