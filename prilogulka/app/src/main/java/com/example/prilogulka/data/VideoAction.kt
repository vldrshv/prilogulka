package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class VideoAction (
    @SerializedName("user_id")
    var userId: Int = 0,
    @SerializedName("video_id")
    var videoId: Int = 0
) {
    override fun toString(): String {
        return "VideoAction(userId=$userId, videoId=$videoId)"
    }
}