package com.example.prilogulka.data.userData

import java.io.Serializable

class UserInfo (var user: Auth?) : Serializable {

    constructor() : this (Auth())
    
    override fun toString(): String {
        return "UserInfo(user=$user)"
    }
}
