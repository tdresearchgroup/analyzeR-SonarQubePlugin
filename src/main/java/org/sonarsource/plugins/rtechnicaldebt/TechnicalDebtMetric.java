package org.sonarsource.plugins.rtechnicaldebt;

public class TechnicalDebtMetric {
    String name;
    String description;
    float value;

    TechnicalDebtMetric(String name, float value, String description){
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TechnicalDebtMetric{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value=" + value +
                '}';
    }
}
