package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class UserGiftCard {
    @SerializedName("user_gift_card")
    var card: UserCard = UserCard()
    
    override fun toString(): String {
        return "GiftCard(card=$card)"
    }
}

class UserCard {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("giftcard_id")
    var userId: Int = 0
    @SerializedName("id")
    var cardId: Int = 0
    @SerializedName("serial_number")
    var serialNumber: String = ""
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
    var price: Int = 0
    @SerializedName("is_activated")
    var isActivated: Boolean = false
}