package org.sonarsource.plugins.rtechnicaldebt.measures;

/**
 * Implements per-file metrics for R source files.
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 * @see RMetrics Definitions of all metrics
 */
public class RModuleMetric {

    double CBO,CA,CE,LCOM,MI;

    @Override
    public String toString() {
        return "Module Average = "  +
                "CBO=" + CBO +
                ", CA=" + CA +
                ", CE=" + CE +
                ", MI=" + MI +
                ", LCOM=" + LCOM +
                '}';
    }
}