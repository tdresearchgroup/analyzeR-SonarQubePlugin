/*

Metrics

 */
/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
package org.sonarsource.plugins.rtechnicaldebt.measures;

public class RFileMetric {
    String filename;

    int LOC,NPM,NOF,NSTAF,NMC,NMCI,NMCE,WMC,RFC,CBO,Ca,Ce,LCOM,CAM,NPRIF,NPRIM;
    Float DAM,AMC,MI;

    @Override
    public String toString() {
        return "RFileMetric{" +
                "filename='" + filename + '\'' +
                ", LOC=" + LOC +
                ", NPM=" + NPM +
                ", NOF=" + NOF +
                ", NSTAF=" + NSTAF +
                ", NMC=" + NMC +
                ", NMCI=" + NMCI +
                ", NMCE=" + NMCE +
                ", WMC=" + WMC +
                ", RFC=" + RFC +
                ", CBO=" + CBO +
                ", Ca=" + Ca +
                ", Ce=" + Ce +
                ", LCOM=" + LCOM +
                ", CAM=" + CAM +
                ", DAM=" + DAM +
                ", NPRIF=" + NPRIF +
                ", NPRIM=" + NPRIM +
                ", AMC=" + AMC +
                ", MI=" + MI +
                '}';
    }
}