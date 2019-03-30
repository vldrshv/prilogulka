package com.example.prilogulka.data.userData

import java.io.Serializable

class Auth (var id: Int,
           email: String?,
           var created_at: String?,
           var updated_at: String?,
           name: String?,
           lastname: String?,
           location: String?,
           birthday: String?,
           sex: String?,
           var pin: String?,
           var emailChecked: Boolean,
           var lastDateOnline: String?,
            current_video_coeff: Double,
            current_balance: Double,
            location_coeff: Double,
            user_coeff: Double
) : Serializable, User(email, name, lastname, location, birthday, sex, current_video_coeff,
        current_balance, location_coeff, user_coeff) {
    
    constructor() : this(0, "", "", "", "", "", "",
            "", "", "", false, "", 0.0,
            0.0, 0.0,0.0)
    
    override fun toString(): String {
        return "Auth(id=$id, " +
                "created_at=$created_at, " +
                "updated_at=$updated_at, " +
                "pin=$pin, " +
                "emailChecked=$emailChecked, " +
                "lastDateOnline=$lastDateOnline)" +
                super.toString()
    }
    
    
}