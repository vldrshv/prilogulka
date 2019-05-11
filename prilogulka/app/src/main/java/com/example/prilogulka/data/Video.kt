package com.example.prilogulka.data

import com.google.gson.annotations.SerializedName

class Video {
    @SerializedName("video")
    val videoItem: VideoItem = VideoItem()
    @SerializedName("videos")
    val videoItemList: Array<VideoItem> = arrayOf()
    
    override fun toString(): String {
        return "Video(videoItem=$videoItem, videoItemList.size=${videoItemList.size})"
    }
    
}

class VideoItem {
//    @SerializedName("ad_compaing_ids")
//    var adCompaniesIdList: List<Int> = arrayListOf()
    @SerializedName("b2b_client_id")
    var b2bClientId: Int = 0
    @SerializedName("created_at")
    var createdAt: String = ""
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String = ""
    @SerializedName("url")
    var url: String = ""
    @SerializedName("watch_counter")
    var watchCounter: Int = 0
    
    override fun toString(): String {
        return "VideoItem(b2bClientId=$b2bClientId, createdAt='$createdAt', id=$id, name='$name', url='$url', watchCounter=$watchCounter)"
    }
//    @SerializedName("price")
//    var price: Int = 0


}