package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

/**
 * Implements per-project metrics for R source files.
 * Contains a list of per-file metrics - for each file in the project
 * Contains a list of per-class metrics - for each class in the project
 * A module-wide set of metrics.
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 * @see RMetrics Definitions of all metrics
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






