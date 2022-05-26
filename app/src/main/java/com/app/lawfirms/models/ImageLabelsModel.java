package com.app.lawfirms.models;

import java.util.ArrayList;

public class ImageLabelsModel {
    private String key = "";
    private String image = "";
    private ArrayList<LabelModel> labels = new ArrayList<>();

    public ImageLabelsModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<LabelModel> labels) {
        this.labels = labels;
    }

    public static class LabelModel {
        private String text = "";
        private float confidence;

        public LabelModel() {
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public float getConfidence() {
            return confidence;
        }

        public void setConfidence(float confidence) {
            this.confidence = confidence;
        }
    }

}


