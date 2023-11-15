package hu.bme.aut.N88G93.shoppinglist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewItemViewModel(application: Application) : AndroidViewModel(application) {
    private val itemDao = ShoppingList.getDatabase(application).itemDao()
    val allItems: LiveData<List<ItemEntity>> = itemDao.getAllItems()
    fun addItem(item: ItemEntity) {
        viewModelScope.launch {
            itemDao.insertItem(item)
        }
    }
    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch {
            itemDao.deleteItem(item)
        }
    }
    fun deleteAllItems() {
        viewModelScope.launch {
            itemDao.deleteAllItems()
        }
    }
    fun updateItem(item: ItemEntity) {
        Log.d("NewItemViewModel", "Updating item with id: ${item.id}")
        viewModelScope.launch {
            itemDao.updateItem(item)
        }
    }

}

