package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

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
    public String getCBO() {
        return  className + "(" + CBO + ')';
    }
}
