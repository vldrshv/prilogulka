package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class UserGiftCard {
    @SerializedName("users_gift_card", alternate = arrayOf("gift_card"))
    var card: UserCard = UserCard()
    
    override fun toString(): String {
        return "GiftCard(card=$card)"
    }
}

class UserCard {
    @SerializedName("user_id")
    var userId: Int = 0
    @SerializedName("id")
    var id: Int = -1
    @SerializedName("giftcard_id")
    var cardId: Int = 0
    @SerializedName("price")
    var price: Int = 0
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

    @SerializedName("is_activated")
    var isActivated: Boolean = false

    override fun toString(): String {
        return "UserCard(userId=$userId, id=$id, cardId=$cardId, price=$price, serialNumber='$serialNumber', dueDate='$dueDate', imageUrl='$imageUrl', brand='$brand', vendor='$vendor', description='$description', isActivated=$isActivated)"
    }


}