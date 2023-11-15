package hu.bme.aut.N88G93.shoppinglist

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Currency

class DetailActivity : AppCompatActivity()
{
    private lateinit var itemNameTextView: TextView
    private lateinit var itemDescriptionTextView: TextView
    private lateinit var estimatedPriceTextView: TextView
    private lateinit var isBoughtCheckBox: CheckBox
    private lateinit var currencySpinner: Spinner
    private var exchangeRates: Map<String, Double>? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        itemNameTextView = findViewById(R.id.detailName)
        itemDescriptionTextView = findViewById(R.id.detailDescription)
        estimatedPriceTextView = findViewById(R.id.detailEstimatedPrice)
        currencySpinner = findViewById(R.id.currencySpinner)
        val currencies = arrayOf("HUF", "USD", "EUR", "GBP", "INR") // Add more currencies as needed
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currencySpinner.adapter = adapter
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCurrency = currencies[position]
                Log.d("DetailActivity", "Selected Currency: $selectedCurrency")
                updateConvertedPrice(selectedCurrency)


            //isBoughtCheckBox = findViewById(R.id.detailIsBought)

            // Retrieve item information from the intent
            val itemName = intent.getStringExtra("ITEM_NAME")
            val itemDescription = intent.getStringExtra("ITEM_DESCRIPTION")
            val estimatedPrice = intent.getFloatExtra("ITEM_ESTIMATED_PRICE", 0.0f)
            val isBought = intent.getBooleanExtra("ITEM_IS_BOUGHT", false)

            // Display item details in your layout
            displayItemDetails(itemName, itemDescription, estimatedPrice)

            fetchExchangeRates("HUF")
        }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }
    private fun fetchExchangeRates(baseCurrency: String) {
        exchangeRates = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.getExchangeRates(baseCurrency)
                exchangeRates = response.rates

                // After fetching exchange rates, update the UI with the selected currency
                runOnUiThread {
                    val selectedCurrency = currencySpinner.selectedItem.toString()
                    updateConvertedPrice(selectedCurrency)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun displayItemDetails(
        itemName: String?,
        itemDescription: String?,
        estimatedPrice: Float
    )
    {
        itemNameTextView.text = itemName
        itemDescriptionTextView.text = itemDescription
        estimatedPriceTextView.text = "Price: $estimatedPrice HUF"
    }
    private fun updateConvertedPrice(selectedCurrency: String)
    {
        val originalPriceHUF = intent.getFloatExtra("ITEM_ESTIMATED_PRICE", 0.0f)
        val conversionRate = exchangeRates?.get(selectedCurrency) ?: 1.0
        val convertedPrice = originalPriceHUF * conversionRate
        val formattedConvertedPrice = String.format("%.4f", convertedPrice)
        estimatedPriceTextView.text = "Price: $formattedConvertedPrice $selectedCurrency"
        Log.d("DetailActivity", "updateConvertedPrice: ${estimatedPriceTextView.text} ")
    }
}


