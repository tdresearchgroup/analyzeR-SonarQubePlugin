package org.sonarsource.plugins.rtechnicaldebt.measures;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.rtechnicaldebt.RPlugin;
import org.sonarsource.plugins.rtechnicaldebt.languages.R;
import org.sonar.api.batch.fs.FileSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;

public class RMetricsSensor implements Sensor {
    private static Logger sensorLogger = Loggers.get(RMetricsSensor.class);


    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorLogger.info("Describe()");
        sensorDescriptor.name("Technical Debt Sensor for the R Language").onlyOnLanguage(R.KEY);
    }

    @Override
    public void execute(SensorContext sensorContext) {
        sensorLogger.info("Sensor execute()");

        FileSystem fs = sensorContext.fileSystem();

        // Reading metrics
        Optional<String> rscriptOutput = sensorContext.config().get(RPlugin.PROPERTY_METRICS_FILE);

        RProjectMetric data ;

        // Skip missing entries
        if (!rscriptOutput.isPresent()){
            sensorLogger.warn("TechDebt Metrics file is not found -> ",rscriptOutput);
            //System.out.println("TechDebt Metrics file is not found -> "+rscriptOutput);
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


            ArrayList<InputFile> inputfiles = new ArrayList<>();
            fs.inputFiles(fs.predicates().and(fs.predicates().hasType(InputFile.Type.MAIN), fs.predicates().hasLanguage(R.KEY)))
                    .forEach(file -> {
                        //countLines(sensorContext, file);
                        updateMetrics(sensorContext, data, file);
                        System.out.println("Processing File name " + file.toString());

                        inputfiles.add(file);
                    });
            System.out.println(inputfiles);
        }
    }



    public RProjectMetric parse(String filename) {

        Gson gson = new Gson();
        String buffer;
        try {
            buffer = Files.readString(Paths.get(filename));
            RProjectMetric data = gson.fromJson(buffer, RProjectMetric.class);
            return data;

        }catch (IOException e){
                System.out.println("ERROR READING Metrics FILE CONTENTS");
                e.printStackTrace();
        }
        return new RProjectMetric();
    }

    // find the file pacific
    public RFileMetric findFileMetric(RProjectMetric data,String  filename) {
        for (RFileMetric fm:data.metrics) {
            //System.out.println("Loop File name " + fm.filename);
            if (fm.filename.equals(filename))
                return fm;
        }
        return null;
    }


    public void updateMetrics(SensorContext sensorContext, RProjectMetric data,InputFile inputFile){

        try
        {
            String filename = inputFile.toString();
            RFileMetric fm = findFileMetric(data,filename);
            if (fm != null) {

                sensorContext.<Integer>newMeasure().withValue(fm.LOC).forMetric(RMetrics.LINES_OF_CODE).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NPM).forMetric(RMetrics.NUMBER_PRIVATE_METHODS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NOF).forMetric(RMetrics.NUMBER_OF_FIELDS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NSTAF).forMetric(RMetrics.NUMBER_STATIC_FIELDS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NMC).forMetric(RMetrics.NUMBER_METHOD_CALLS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NMCI).forMetric(RMetrics.NUMBER_METHOD_CALLS_INTERNAL).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NMCE).forMetric(RMetrics.NUMBER_METHOD_CALLS_EXTERNAL).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.WMC).forMetric(RMetrics.WEIGHTED_METHODS_PER_CLASS).on(inputFile).save();
                sensorContext.<Float>newMeasure().withValue(fm.AMC).forMetric(RMetrics.AVERAGE_METHOD_COMPLEXITY).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.RFC).forMetric(RMetrics.RESPONSE_FOR_CLASS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.CBO).forMetric(RMetrics.COUPLING_BETWEEN_OBJECTS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.Ca).forMetric(RMetrics.AFFERENT_COUPLING).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.Ce).forMetric(RMetrics.EFFERENT_COUPLING).on(inputFile).save();
                sensorContext.<Float>newMeasure().withValue(fm.MI).forMetric(RMetrics.MARTINS_INSTABILITY).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.LCOM).forMetric(RMetrics.LACK_OF_COHESION_IN_METHODS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.CAM).forMetric(RMetrics.COHESION_AMONG_METHODS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.DAM).forMetric(RMetrics.DATA_ACCESS_METRICS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NPRIF).forMetric(RMetrics.NUMBER_PRIVATE_FIELDS).on(inputFile).save();
                sensorContext.<Integer>newMeasure().withValue(fm.NPRIM).forMetric(RMetrics.NUMBER_PRIVATE_METHODS).on(inputFile).save();
            }
        } catch (Exception e) {
            sensorLogger.warn("Error in readMetrics, which is required to get the measures "+ e.getMessage());
            e.printStackTrace();
        }
    }
}

