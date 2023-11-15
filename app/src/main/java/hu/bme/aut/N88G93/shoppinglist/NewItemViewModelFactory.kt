package hu.bme.aut.N88G93.shoppinglist
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.app.Application

class NewItemViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewItemViewModel::class.java)) {
            return NewItemViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}