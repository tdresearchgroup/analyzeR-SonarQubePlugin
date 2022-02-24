/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 * @see RMetrics for definitions of all metrics
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

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