package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class GiftCardK {
    @SerializedName("giftcard")
    var card: Card = Card()
    
    override fun toString(): String {
        return "GiftCardK(card=$card)"
    }
}

class Card {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("serial_number")
    var serialNumber: String = ""
    @SerializedName("ad_compaing_id")
    var companyAdvertisementId: Int = 0
    @SerializedName("due_date")
    var dueDate: String = ""
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
    
    override fun toString(): String {
        return "Card(id=$id, serialNumber='$serialNumber', " +
                "companyAdvertisementId=$companyAdvertisementId, dueDate='$dueDate', " +
                "imageUrl='$imageUrl', brand='$brand', vendor='$vendor', " +
                "description='$description', priceArray=$priceArray)"
    }
    
    
}