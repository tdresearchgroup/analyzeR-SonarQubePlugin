package org.sonarsource.plugins.rtechnicaldebt.measures;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.rtechnicaldebt.RPlugin;
import org.sonarsource.plugins.rtechnicaldebt.languages.R;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.measures.Metrics;

import java.util.Scanner;
import java.nio.charset.StandardCharsets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<String> tdOutputProperty = sensorContext.config().get(RPlugin.PROPERTY_METRICS_FILE);

        // Skip missing entries
        if (!tdOutputProperty.isPresent()){
            sensorLogger.warn("TechDebt Metrics file is not found -> ",tdOutputProperty);
            System.out.println("TechDebt Metrics file is not found -> "+tdOutputProperty);
        }

        else{
            String metricfile = tdOutputProperty.get();
            List<String> filedata;
            try {
                filedata = Files.readAllLines(Paths.get(metricfile));

            } catch (IOException e) {
                sensorLogger.warn("Error Reading "+metricfile);
                sensorLogger.warn(e.getMessage());
                e.printStackTrace();
                return;
            }
            System.out.println(filedata);
            sensorLogger.info("Read td-output.json");

            /*
            String json = new String(filedata);
            System.out.println(json);
             */

        }

        // TODO - See if there another way to get an overview tab working in sonarqube
        // A lines of Code value which will get the overview tab working in sonarqube

        ArrayList<InputFile> inputfiles = new ArrayList<>();
        fs.inputFiles(fs.predicates().and(fs.predicates().hasType(InputFile.Type.MAIN),fs.predicates().hasLanguage(R.KEY)))
                .forEach(file -> {
                    //countLines(sensorContext, file);
                    readMetrics(sensorContext, file);
                    inputfiles.add(file);
                });
        System.out.println(inputfiles);

    }

    public void countLines(SensorContext sensorContext, InputFile inputFile){
        try {
            int numlines = inputFile.lines();
            sensorContext.<Integer>newMeasure().withValue(numlines).forMetric(CoreMetrics.NCLOC).on(inputFile).save();
        } catch (Exception e) {
            sensorLogger.warn("Error in countLines, which is required to get a new tab in sonarqube "+ e.getMessage());
            e.printStackTrace();
        }
    }


    /*

        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
        stream.forEach(System.out::println);
    }
        catch (IOException e) {
        e.printStackTrace();
    }

     */
    public void readMetrics(SensorContext sensorContext, InputFile inputFile){

        try //(Scanner scanner = new Scanner(inputFile, StandardCharsets.UTF_8))
        {
            /*
            int [] values = new int [100];
            int i = 0;
            while(scanner.hasNextInt()) {
                value[i++] = scanner.nextInt();
            }

             */
            sensorContext.<Integer>newMeasure().withValue(100).forMetric(RMetrics.RTD_LOC_SIZE).on(inputFile).save();
            sensorContext.<Integer>newMeasure().withValue(200).forMetric(RMetrics.RTD_NMC_SIZE).on(inputFile).save();
        } catch (Exception e) {
            sensorLogger.warn("Error in readMetrics, which is required to get a new tab in sonarqube "+ e.getMessage());
            e.printStackTrace();
        }
    }
}
