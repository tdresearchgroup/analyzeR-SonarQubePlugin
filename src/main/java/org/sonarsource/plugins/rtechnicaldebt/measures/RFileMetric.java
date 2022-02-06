/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
package org.sonarsource.plugins.rtechnicaldebt.measures;

/**
 * Declares all the individual File Metrics.
 */
public class RFileMetric {
    String filename;
    // Defining all the file based metrics relevant to this project
    int LOC,NPM,NOF,NMC,NMCI,NMCE,WMC,RFC,NPRIF,NPRIM;
    Float DAM,AMC,MI,CBO,Ca,Ce,LCOM;

    @Override
    public String toString() {
        return "RFileMetric{" +
                "filename='" + filename + '\'' +
                ", LOC=" + LOC +
                ", NPM=" + NPM +
                ", NOF=" + NOF +
                ", NMC=" + NMC +
                ", NMCI=" + NMCI +
                ", NMCE=" + NMCE +
                ", WMC=" + WMC +
                ", RFC=" + RFC +
                ", NPRIF=" + NPRIF +
                ", NPRIM=" + NPRIM +
                ", DAM=" + DAM +
                ", AMC=" + AMC +
                ", MI=" + MI +
                ", CBO=" + CBO +
                ", Ca=" + Ca +
                ", Ce=" + Ce +
                ", LCOM=" + LCOM +
                '}';
    }
}