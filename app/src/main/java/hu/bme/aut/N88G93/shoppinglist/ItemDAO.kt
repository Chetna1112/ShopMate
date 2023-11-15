package hu.bme.aut.N88G93.shoppinglist
// ItemDao.kt
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {

    @Insert
     suspend fun insertItem(item: ItemEntity)

     @Delete
     suspend fun deleteItem(item: ItemEntity)

    @Query("SELECT * FROM item_table")
    fun getAllItems(): LiveData<List<ItemEntity>>

    @Query("DELETE FROM item_table")
    suspend fun deleteAllItems()

    @Update
    suspend fun updateItem(item: ItemEntity)


}
