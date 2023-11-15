package hu.bme.aut.N88G93.shoppinglist
// App.kt
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
class ShoppingList : Application() {

    companion object {
        private var INSTANCE: ItemDatabase? = null

        // Make sure to initialize the database instance properly
        fun getDatabase(context: Context): ItemDatabase {
            if (INSTANCE == null) {
                synchronized(ItemDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ItemDatabase::class.java, "item_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
