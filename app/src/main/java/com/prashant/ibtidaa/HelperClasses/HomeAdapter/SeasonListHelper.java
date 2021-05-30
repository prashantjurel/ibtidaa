package com.prashant.ibtidaa.HelperClasses.HomeAdapter;

public class SeasonListHelper {

    String seasonAlbumArt,author,title;

    public SeasonListHelper(String seasonAlbumArt, String author, String title){
        this.seasonAlbumArt=seasonAlbumArt;
        this.author = author;
        this.title = title;
    }

    public String getSeasonAlbumArt() {
        return seasonAlbumArt;
    }

    public void setSeasonAlbumArt(String seasonAlbumArt) {
        this.seasonAlbumArt = seasonAlbumArt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
