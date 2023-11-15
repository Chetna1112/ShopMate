package hu.bme.aut.N88G93.shoppinglist
// ItemEntity.kt
// ItemEntity.kt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "itemName")
    val name: String,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "estimatedPrice")
    val estimatedPriceHUF: Float,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "isBought")
    var isBought: Boolean
) {
    fun toDomainModel(): Item {
        return Item(
            name = name,
            category = category,
            estimatedPriceHUF = estimatedPriceHUF,
            description = description,
            isBought = isBought
        )
    }
}
