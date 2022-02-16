/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

public class RFileMetric {
    String filename;

    int LOC,NPM,NOF,NMC,NMCI,NMCE,WMC,RFC,NPRIF,NPRIM;
    double DAM,AMC;


    @Override
    public String toString() {
        return "RFileMetric{" +
                "filename='" + filename + '\'' +
                ", LOC=" + LOC +
                ", NPM=" + NPM +
                ", NOF=" + NOF +
                ",\n NMC=" + NMC +
                ", NMCI=" + NMCI +
                ", NMCE=" + NMCE +
                ", WMC=" + WMC +
                ", RFC=" + RFC +
                ",\n DAM=" + DAM +
                ", NPRIF=" + NPRIF +
                ", NPRIM=" + NPRIM +
                ", AMC=" + AMC +
                '}';
    }
}