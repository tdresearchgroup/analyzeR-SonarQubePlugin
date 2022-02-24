package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 * Instantiates
 * @see RMetrics for definitions of all metrics
 */

public class RProjectMetric {
    String  ScriptVersion;
    List<RFileMetric> metrics;
    List<RClassMetric> classmetrics;
    RModuleMetric moduleMetrics;


    @Override
    public String toString() {
        return "RProjectMetric{" + '\n' +
                "ScriptVersion='" + ScriptVersion + '\n' +
                "metrics=" + metrics + '\n' +
                "classmetrics=" + classmetrics + '\n' +
                "projectmetrics=" + moduleMetrics + '\n' +
                '}';
    }
}






