package com.example.ecoactivity;

public class ActivityItem {
    private String description;
    private String explanation; // Penjelasan tambahan
    private int iconResId;

    public ActivityItem(String description, String explanation, int iconResId) {
        this.description = description;
        this.explanation = explanation;
        this.iconResId = iconResId;
    }

    public String getDescription() {
        return description;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getIconResId() {
        return iconResId;
    }
}


