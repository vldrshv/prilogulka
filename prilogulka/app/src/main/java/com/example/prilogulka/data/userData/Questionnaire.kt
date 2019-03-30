package com.example.prilogulka.data.userData


class Questionnaire (var user_id: Int) {
    var education: String = ""
    var nationality: String = ""
    var income: String = ""
    var family: String = ""
    var children: Int = -1
    var car: Int = -1
    var motorcycle: Int = -1
    var computer_games: Int = -1
    var banks: Int = -1
    var cinema: Int = -1
    var alcohol: String = ""
    var tobacco: String = ""
    var vegeterian: Int = -1
    var sport: Int = -1
//    var current_video_coeff: String = ""
//    var current_balance: String = ""
    
    fun isEmpty() : Boolean {
        return education.equals("")
                || nationality.equals("")
                || income.equals("")
                || family.equals("")
                || children == -1
                || car == -1
                || motorcycle == -1
                || computer_games == -1
                || banks == -1
                || cinema == -1
                || alcohol.equals("")
                || tobacco.equals("")
                || vegeterian  == -1
                || sport == -1
    }
    
    override fun toString(): String {
        return "Questionnaire(\n" +
                "user_id='$user_id',\n " +
                "education='$education',\n" +
                "nationality='$nationality',\n " +
                "income='$income',\n" +
                "family='$family',\n" +
                "children=$children,\n" +
                "car=$car,\n" +
                "motorcycle=$motorcycle,\n" +
                "computer_games=$computer_games,\n" +
                "banks=$banks,\n" +
                "cinema=$cinema,\n" +
                "alcohol='$alcohol',\n" +
                "tobacco='$tobacco',\n" +
                "vegeterian=$vegeterian,\n" +
                "sport=$sport)\n" // +
//                "current_video_coeff='$current_video_coeff',\n" +
//                "current_balance='$current_balance')\n"
    }
    
    
}