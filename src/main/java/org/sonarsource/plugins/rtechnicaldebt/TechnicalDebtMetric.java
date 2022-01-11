package org.sonarsource.plugins.rtechnicaldebt;

public class TechnicalDebtMetric {
    String filename,short_name,category,description, full_name;
    float value;

    TechnicalDebtMetric(String filename,String short_name,String category,String description, String full_name, float value){
        this.value = value;
        this.filename = filename;
        this.short_name = short_name;
        this.category = category;
        this.description = description;
        this.full_name =  full_name;
    }

    public float getValue() {
        return value;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getFilename() {
        return filename;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Override
    public String toString() {
        return "TechnicalDebtMetric{" +
                "filename='" + filename + '\'' +
                ", short_name='" + short_name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
