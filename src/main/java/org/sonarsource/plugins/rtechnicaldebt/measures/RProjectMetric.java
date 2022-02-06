/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

public class RProjectMetric {
    String  ScriptVersion;
    // List of metrics, which will be loaded in the sensor, this is project wide.
    List<RFileMetric> metrics;

    @Override
    public String toString() {
        return "RProjectMetric{" +
                "ScriptVersion='" + ScriptVersion + '\'' +
                ", metrics=" + metrics +
                '}';
    }
}






