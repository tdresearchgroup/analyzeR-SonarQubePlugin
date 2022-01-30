/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

public class RProjectMetric {
    String  ScriptVersion;
    List<RFileMetric> metrics;  // LOAD THE Metrics

    @Override
    public String toString() {
        return "RProjectMetric{" +
                "ScriptVersion='" + ScriptVersion + '\'' +
                ", metrics=" + metrics +
                '}';
    }
}






