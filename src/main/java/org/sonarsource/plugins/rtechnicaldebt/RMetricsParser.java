package org.sonarsource.plugins.rtechnicaldebt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RMetricsParser {

    // String JSON = "local-testing-resources/r_techdebt_output.json";
    List<TechnicalDebtMetric> metrics;

    public RMetricsParser() throws IOException {
        // TODO - Make sensor use this. Causes Null pointer exception, so this needs to stay till fixed.
        metrics = new ArrayList<>();
        //this.metrics = getMetrics(JSON);

        System.out.println(this);
        String currentPath = new java.io.File(".").getCanonicalPath();
        System.out.println("Current dir:" + currentPath);

        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);
    }

    public List<TechnicalDebtMetric> getMetrics(InputFile file) throws IOException {

        System.out.println("READING FILE -> "+ file.toString());

        Reader reader = Files.newBufferedReader(file.path());

        List<TechnicalDebtMetric> metrics = new Gson().fromJson(reader,new TypeToken<List<TechnicalDebtMetric>>() {}.getType());

        metrics.forEach(System.out::println);

        reader.close();

        return metrics;
    }

}
