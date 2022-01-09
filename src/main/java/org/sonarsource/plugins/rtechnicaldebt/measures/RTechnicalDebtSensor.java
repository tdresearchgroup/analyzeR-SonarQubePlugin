package org.sonarsource.plugins.rtechnicaldebt.measures;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.plugins.rtechnicaldebt.languages.R;

import java.nio.file.FileSystem;

public class RTechnicalDebtSensor implements Sensor {
    private static Logger sensorLogger = Loggers.get(RTechnicalDebtSensor.class);

    private FileSystem files;

    RTechnicalDebtSensor(FileSystem fs){
        sensorLogger.info("Initializing R-Technical Debt Sensor");
        this.files = fs;
    }
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorLogger.info("Describe()");
        sensorDescriptor.name("Technical Debt Sensor for the R Language").onlyOnLanguage(R.KEY);
    }

    @Override
    public void execute(SensorContext sensorContext) {
        sensorLogger.info("Sensor execute()");

    }
}
