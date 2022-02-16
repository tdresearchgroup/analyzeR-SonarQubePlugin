/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

public class RModuleMetric {

    double CBO,CA,CE,LCOM,MI;        // Project average metrics


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