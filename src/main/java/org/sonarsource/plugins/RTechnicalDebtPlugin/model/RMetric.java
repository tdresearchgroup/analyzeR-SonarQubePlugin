package org.sonarsource.plugins.RTechnicalDebtPlugin.model;

public class RMetric {
    String metric;
    String filename;
    float value;

    public RMetric(String metric_name, String filename, float value){
        this.filename =filename;
        this.metric = metric_name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "RMetric{" +
                "metric='" + metric + '\'' +
                ", filename='" + filename + '\'' +
                ", value=" + value +
                '}';
    }
}
