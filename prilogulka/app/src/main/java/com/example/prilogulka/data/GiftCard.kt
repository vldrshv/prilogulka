package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class GiftCard {
    @SerializedName("giftcard")
    var card: Card = Card()
    
    override fun toString(): String {
        return "GiftCard(card=$card)"
    }
}

class Card {
    
    @SerializedName("id")
    var cardId: Int = 0
    @SerializedName("serial_number")
    var serialNumber: String = ""
    @SerializedName("ad_compaing_id")
    var companyAdvertisementId: Int = 0
    @SerializedName("due_date")
    var dueDate: String = ""
    var dayBought: String = ""
    @SerializedName("image_url")
    var imageUrl: String = ""
    @SerializedName("brand")
    var brand: String = ""
    @SerializedName("vendor")
    var vendor: String = ""
    @SerializedName("description")
    var description: String = ""
    @SerializedName("price")
    var priceArray: List<String> = arrayListOf()
    
    var priceBronze: Int = 0
    var priceSilver: Int = 0
    var priceGold: Int = 0
    
    //todo НУЖНА ЛИ СОРТИРОВКА
    fun setPrices() {
        var price = arrayOf(0, 0, 0)
        for (i in 0 until priceArray.size)
            price[i] = if (priceArray[i] == "") 0 else Integer.parseInt(priceArray[i])
        price.sort()
        priceBronze = price[0]
        priceSilver = price[1]
        priceGold = price[2]
    }
    
    override fun toString(): String {
        return "Card(cardId=$cardId, serialNumber='$serialNumber', " +
                "companyAdvertisementId=$companyAdvertisementId, dueDate='$dueDate'," +
                "dayBought='$dayBought', imageUrl='$imageUrl', " +
                "brand='$brand', vendor='$vendor', description='$description', " +
                "priceArray=$priceArray, priceBronze=$priceBronze, priceSilver=$priceSilver, priceGold=$priceGold)"
    }
    
    
}