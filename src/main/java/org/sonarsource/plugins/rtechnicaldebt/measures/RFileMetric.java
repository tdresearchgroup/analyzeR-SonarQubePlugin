/*

Metrics

 */
package org.sonarsource.plugins.rtechnicaldebt.measures;

public class RFileMetric {
    String filename;

    int LOC;  // Lines of Code
    int NMC;  // Number of Method Calls
    float MI;

    @Override
    public String toString() {
        return "RFileMetric{" +
                "filename='" + filename + '\'' +
                ", LOC=" + LOC +
                ", NMC=" + NMC +
                ", MI=" + MI +
                '}';
    }
}