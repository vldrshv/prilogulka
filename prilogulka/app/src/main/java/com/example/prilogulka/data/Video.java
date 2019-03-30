package com.example.prilogulka.data;

public class Video {
    private int id;
    private String videoId;
    private int watchPoints;
    private String userWatched;
    private String watchDate;

    public Video (int id, String userWatched, String videoId, int watchPoints, String watchDate){
        this.id = id;
        this.userWatched = userWatched;
        this.videoId = videoId;
        this.watchPoints = watchPoints;
        this.watchDate = watchDate;
    }
    public Video (String userWatched, String videoId, int watchPoints, String watchDate){
        this.userWatched = userWatched;
        this.videoId = videoId;
        this.watchPoints = watchPoints;
        this.watchDate = watchDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int getWatchPoints() {
        return watchPoints;
    }

    public void setWatchPoints(int watchPoints) {
        this.watchPoints = watchPoints;
    }

    public String getUserWatched() {
        return userWatched;
    }

    public void setUserWatched(String userWatched) {
        this.userWatched = userWatched;
    }

    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId='" + videoId + '\'' +
                ", watchPoints=" + watchPoints +
                ", userWatched='" + userWatched + '\'' +
                ", watchDate='" + watchDate + '\'' +
                '}';
    }
}
