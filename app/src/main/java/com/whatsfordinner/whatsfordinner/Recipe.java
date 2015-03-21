package com.whatsfordinner.whatsfordinner;

/**
 * Created by marck on 21.03.15.
 */
public class Recipe {
    private String title;
    private String imageSrc;
    private String link;

    public Recipe(String title, String imageSrc, String link) {
        this.title = title;
        this.imageSrc = imageSrc;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public String getLink() {
        return link;
    }
}
