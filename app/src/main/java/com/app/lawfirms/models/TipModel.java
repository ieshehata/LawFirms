package com.app.lawfirms.models;

import java.util.Date;

public class TipModel {
    private String key;
    private String title = "";
    private String text = "";
    private String image = "";
    private boolean repeat;
    private Date date;
    private int iterate;


    public TipModel() {
    }

    public TipModel(String key, String title, String text, boolean repeat, Date date, int iterate) {
        this.key = key;
        this.title = title;
        this.text = text;
        this.repeat = repeat;
        this.date = date;
        this.iterate = iterate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIterate() {
        return iterate;
    }

    public void setIterate(int iterate) {
        this.iterate = iterate;
    }
}
