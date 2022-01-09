package org.sonarsource.plugins.rtechnicaldebt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class getMetricsFromParsedJSON {

    String JSON = "testjson.json";
    List<TechnicalDebtMetric> metrics;

    public getMetricsFromParsedJSON() throws IOException {
        // this.metrics = getMetrics(JSON);
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
