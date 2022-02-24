package org.sonarsource.plugins.rtechnicaldebt.measures;

/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 * Implements Class metrics for each R class - CBO, Ca, CE, MI, LCOM
 * @see RMetrics for definitions of all metrics
 */
public class RClassMetric {
    String  className;
    int CBO,Ca,Ce;
    double MI,LCOM;

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

    public String getClassName() {
        return className ;
    }

    public String getValue() {
        return "CBO : " + CBO +
                ", CA : " + Ca +
                ", CE : " + Ce +
                ", MI : " + MI +
                ", LCOM : " + LCOM ;
    }
}
