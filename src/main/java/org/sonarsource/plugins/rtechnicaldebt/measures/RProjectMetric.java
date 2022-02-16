/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

public class RProjectMetric {
    String  ScriptVersion;
    List<RFileMetric> metrics;       // LOAD THE file based Metrics
    List<RClassMetric> classmetrics; // All class specific metrics

    RModuleMetric projectmetrics ;


    @Override
    public String toString() {
        return "Project = { "  + metrics.toString() + classmetrics.toString() + projectmetrics.toString() + '}';
    }
}






