package com.example.prilogulka.data.userData

import java.io.Serializable

open class User : Serializable {
    var email: String?
    var name: String?
    var lastname: String?
    var location: String?
    var birthday: String?
    var sex: Int
    var current_video_coeff: Double
    var current_balance: Double
    var location_coeff: Double
    var user_coeff: Double
    
    /**
        TODO: добавить данные соответсвующие схеме йобобо.хмл
        current_video_coeff: Double     -- текущий коэффициент видео (коэф_меняющийся * коэф_анкеты)
        current_balance: Double         -- текущий балланс
        location_coeff: Int             -- число, представляющее предпочтения пользователя по локации
        user_coeff: Int                 -- коэффициент предпочтений юзера
     */
    
    constructor(_email: String?, _name: String?, _lastname: String?, _location: String?,
                _birthday: String?, _sex: String?, _current_video_coeff: Double,
                _current_balance: Double, _location_coeff: Double, _user_coeff: Double){
        this.email = _email
        this.name = _name
        this.lastname = _lastname
        this.location = _location
        this.birthday = _birthday
        this.sex = convertSex(_sex)
        this.current_video_coeff = _current_video_coeff
        this.current_balance = _current_balance
        this.location_coeff = _location_coeff
        this.user_coeff = _user_coeff
    }

    private fun convertSex(_sex: String? = "male") : Int = if (_sex.equals("male")) 0 else 1
    private fun convertSex() : String = if (sex == 0) "male" else "female"
    
    override fun toString(): String {
        return "User(email=$email, " +
                "name=$name, " +
                "lastname=$lastname, " +
                "location=$location, " +
                "birthday=$birthday, " +
                "sex=$sex, " +
                "current_video_coeff=$current_video_coeff, " +
                "current_balance=$current_balance, " +
                "location_coeff=$location_coeff, " +
                "user_coeff=$user_coeff)"
    }
    
    
    constructor() : this(null, null, null, null, null, null,
            0.0, 0.0, 0.0, 0.0)


    


}