/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.rtechnicaldebt.RTechnicalDebtPlugin;
import org.sonarsource.plugins.rtechnicaldebt.languages.RLanguageDefinition;
import org.sonar.api.batch.fs.FileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

/**
 * Implementation of a SonarSensor to read the metrics file and extract metrics.
 */
public class RMetricsSensor implements Sensor {

    /**
     * Logger to track any errors in sensor operation.
     */
    private static final Logger sensorLogger = Loggers.get(RMetricsSensor.class);


    /**
     * Setting up a SensorDescriptor object for the sensor.
     * @param sensorDescriptor Descriptor of the R sensor, set a name for it.
     */
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorLogger.info("Describe()");
        sensorDescriptor.name("Technical Debt Sensor for the R Language").onlyOnLanguage(RLanguageDefinition.KEY);
    }

    /**
     * Running the Sensor for this project.
     * Will read the metrics JSON.
     * @param sensorContext
     */
    @Override
    public void execute(SensorContext sensorContext) {
        sensorLogger.info("Sensor execute()");

        FileSystem fs = sensorContext.fileSystem();

        // Reading metrics
        Optional<String> rscriptOutput = sensorContext.config().get(RTechnicalDebtPlugin.PROPERTY_METRICS_FILE);

        RProjectMetric data ;

        // Skip missing entries
        if (!rscriptOutput.isPresent()){
            sensorLogger.warn("TechDebt Metrics file is not found -> ",rscriptOutput);
        }

        else {
            String metricfile = rscriptOutput.get();
            List<String> filedata;
            try {
                filedata = Files.readAllLines(Paths.get(metricfile));

                data = parse(metricfile);
                //System.out.println("Parsed Data " + data.toString());

            } catch (IOException e) {
                sensorLogger.warn("Error Reading " + metricfile);
                sensorLogger.warn(e.getMessage());
                e.printStackTrace();
                return;
            }


            updateProjectMetrics(sensorContext,data);
            ArrayList<InputFile> inputfiles = new ArrayList<>();
            fs.inputFiles(fs.predicates().and(fs.predicates().hasType(InputFile.Type.MAIN), fs.predicates().hasLanguage(RLanguageDefinition.KEY)))
                    .forEach(file -> {
                        updateMetrics(sensorContext, data, file);
                        System.out.println("Processing File name " + file.toString());

                        inputfiles.add(file);
                    });
            System.out.println(inputfiles);
        }
    }

    /**
     * Parses the the metrics file into an RProjectMetric object.
     * @param filename Filename of the metrics file, using a GSON object.
     * @return RProjectMetric object
     */
    public RProjectMetric parse(String  filename) {

        Gson gson = new Gson();

        String buf;
        try {
            buf = Files.readString(Paths.get(filename));
            RProjectMetric data = gson.fromJson(buf, RProjectMetric.class);
            return data;

        }catch (IOException e){
                System.out.println("Error Reading Metrics File "+filename);
                e.printStackTrace();
        }
        return new RProjectMetric();
    }

    /**
     * Find file specific metrics
     * @param data
     * @param filename
     * @return an instance of a RFileMetric Object.
     */
    public RFileMetric findFileMetric(RProjectMetric data,String  filename) {
        for (RFileMetric fm:data.metrics) {
            if ( fm.filename.equals(filename))
                return fm;
        }
        return new RFileMetric();
    }

    /**
     * Update Metrics for the SensorContext.
     * @param sensorContext The sensorContext for the plugin. The measures get added to it.
     * @param data a RProjectMetric instance
     * @param inputFile The inputfile containing the metrics.
     */
    public void updateMetrics(SensorContext sensorContext, RProjectMetric data,InputFile inputFile){
        String filename = inputFile.toString();
        try
        {
            RFileMetric fm = findFileMetric(data,filename);

            if (fm != null) {


                // SET Size Metrics
                sensorContext.<Integer>newMeasure().withValue(fm.LOC).forMetric(RMetrics.LINES_OF_CODE).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NMC).forMetric(RMetrics.NUMBER_METHOD_CALLS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NMCI).forMetric(RMetrics.NUMBER_METHOD_CALLS_INTERNAL).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NMCE).forMetric(RMetrics.NUMBER_METHOD_CALLS_EXTERNAL).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NPM).forMetric(RMetrics.NUMBER_PUBLIC_METHODS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NOF).forMetric(RMetrics.NUMBER_PUBLIC_FIELDS).on(inputFile).save();


                // Set Encapsulation Metrics
                sensorContext.<Double>newMeasure().withValue(fm.DAM).forMetric(RMetrics.DATA_ACCESS_METRICS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NPRIF).forMetric(RMetrics.NUMBER_PRIVATE_FIELDS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NPRIM).forMetric(RMetrics.NUMBER_PRIVATE_METHODS).on(inputFile).save();


                // Set the Complexity Metrics
                sensorContext.<Integer>newMeasure().withValue(fm.WMC).forMetric(RMetrics.WEIGHTED_METHODS_PER_CLASS).on(inputFile).save();
                sensorContext.<Double>newMeasure().withValue(fm.AMC).forMetric(RMetrics.AVERAGE_METHOD_COMPLEXITY).on(inputFile).save();

                // Set the Complexity Metrics
                sensorContext.<Integer>newMeasure().withValue(fm.RFC).forMetric(RMetrics.RESPONSE_FOR_CLASS).on(inputFile).save();

            }
        } catch (Exception e) {
            sensorLogger.warn("Error in readMetrics, which is required to get the measures for "+ filename + " " + e.getMessage());
        }
    }


    /**
     * Updates Project-Wide Metrics for the functions
     * @param context The sensorContext for the plugin. The measures get added to it.
     * @param data a RProjectMetric instance
     */
    public void updateProjectMetrics(SensorContext context, RProjectMetric data) {


        for (RClassMetric cm:data.classmetrics) {
            System.out.println("Class " + cm.toString());

        }

        context.<Double>newMeasure().withValue(data.projectmetrics.CBO).forMetric(RMetrics.COUPLING_BETWEEN_OBJECTS).on(context.project()).save();
        context.<Double>newMeasure().withValue(data.projectmetrics.CA).forMetric(RMetrics.AFFERENT_COUPLING).on(context.project()).save();
        context.<Double>newMeasure().withValue(data.projectmetrics.CE).forMetric(RMetrics.EFFERENT_COUPLING).on(context.project()).save();
        context.<Double>newMeasure().withValue(data.projectmetrics.MI).forMetric(RMetrics.MARTINS_INSTABILITY).on(context.project()).save();
        context.<Double>newMeasure().withValue(data.projectmetrics.LCOM).forMetric(RMetrics.LACK_COHESION_METHODS).on(context.project()).save();


    }
}

