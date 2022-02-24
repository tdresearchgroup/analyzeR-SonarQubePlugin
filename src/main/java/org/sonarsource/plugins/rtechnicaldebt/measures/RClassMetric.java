package org.sonarsource.plugins.rtechnicaldebt.measures;

/**
 * Implements Class metrics for each R class - CBO, Ca, CE, MI, LCOM
 * @see RMetrics Definitions of all metrics
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public class RClassMetric {
    String  className;
    int CBO,Ca,Ce;
    double MI,LCOM;

    /**
     * Gets the name of the class.
     * @return className
     */
    public String getClassName() {
        return className;
    }

    @Override
    public String toString() {
        return "Class = " + className + "{" +
                "CBO=" + CBO +
                ", CA=" + Ca +
                ", CE=" + Ce +
                ", MI=" + MI +
                ", LCOM=" + LCOM +
                '}';
    }
}
