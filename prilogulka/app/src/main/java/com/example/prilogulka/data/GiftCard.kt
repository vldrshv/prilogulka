package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class GiftCard {
    @SerializedName("giftcard", alternate = arrayOf("gift_card"))
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
    @SerializedName("is_activated")
    var isActivated: Boolean = false
    
    var priceBronze: Int = -1
    var priceSilver: Int = -1
    var priceGold: Int = -1
    
    fun setPrices() {
        var priceList: ArrayList<Int> = arrayListOf()
        
        for (i in 0 until priceArray.size)
            priceList.add(if (priceArray[i] == "") continue else Integer.parseInt(priceArray[i]))
        
        priceList.sort()
        for (i in 0 until priceList.size) {
            when (i) {
                0 -> priceBronze = priceList[0]
                1 -> priceSilver = priceList[1]
                2 -> priceGold = priceList[2]
            }
        }
    }
    
    override fun toString(): String {
        return "Card(cardId=$cardId, serialNumber='$serialNumber', " +
                "companyAdvertisementId=$companyAdvertisementId, dueDate='$dueDate'," +
                "dayBought='$dayBought', imageUrl='$imageUrl', " +
                "brand='$brand', vendor='$vendor', description='$description', " +
                "priceArray=$priceArray, priceBronze=$priceBronze, priceSilver=$priceSilver, priceGold=$priceGold)"
    }
    
    
}