package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class VideoAction {
    @SerializedName("user_video_action")
    var userVideoAction: UserVideoAction = UserVideoAction()
    
    override fun toString(): String {
        return "VideoAction(userVideoAction=$userVideoAction)"
    }
    
    
}

class UserVideoAction {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("user_id")
    var userId: Int = 0
    @SerializedName("video_id")
    var videoId: Int = 0
    @SerializedName("was_watched")
    var wasWatched: Int = 1
    @SerializedName("income")
    var income: Int = 0
    
    override fun toString(): String {
        return "UserVideoAction(id=$id, userId=$userId, videoId=$videoId, wasWatched=$wasWatched, income=$income)"
    }
    
}