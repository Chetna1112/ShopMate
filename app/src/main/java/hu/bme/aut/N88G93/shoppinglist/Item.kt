package hu.bme.aut.N88G93.shoppinglist

data class Item(
    val name: String,
    val category: String,
    val estimatedPriceHUF: Float,
    val description: String,
    var isBought: Boolean
){
    fun toItemEntity(): ItemEntity {
        return ItemEntity(
            name = this.name,
            category = this.category,
            estimatedPriceHUF = this.estimatedPriceHUF,
            description = this.description,
            isBought = this.isBought
        )
    }
}
