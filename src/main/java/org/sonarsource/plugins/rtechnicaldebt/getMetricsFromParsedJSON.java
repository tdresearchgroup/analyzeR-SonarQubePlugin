package org.sonarsource.plugins.rtechnicaldebt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class getMetricsFromParsedJSON {

    String JSON = "testjson.json";
    List<TechnicalDebtMetric> metrics;

    public getMetricsFromParsedJSON() throws IOException {
        // this.metrics = getMetrics(JSON);
        System.out.println(this);
        String currentPath = new java.io.File(".").getCanonicalPath();
        System.out.println("Current dir:" + currentPath);

        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" + currentDir);

        metrics = new ArrayList<TechnicalDebtMetric>();
        // TODO - Make sensor use this. Causes Null pointer exception, so this needs to stay till fixed.
    }

    private List<TechnicalDebtMetric> getMetrics(String filename) throws IOException {

        Reader reader = Files.newBufferedReader(Paths.get(filename));

        List<TechnicalDebtMetric> metrics = new Gson().fromJson(reader,new TypeToken<List<TechnicalDebtMetric>>() {}.getType());

        metrics.forEach(System.out::println);

        reader.close();

        return metrics;
    }

    public List<TechnicalDebtMetric> getMetrics() {
        return metrics;
    }

    public void setJSON(String JSON) {
        this.JSON = JSON;
    }
}
