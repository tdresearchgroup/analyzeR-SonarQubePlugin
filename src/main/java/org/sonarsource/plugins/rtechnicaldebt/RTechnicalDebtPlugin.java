package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonarsource.plugins.rtechnicaldebt.measures.RMetrics;
import org.sonarsource.plugins.rtechnicaldebt.measures.RMetricsSensor;
import org.sonarsource.plugins.rtechnicaldebt.measures.cumulative.*;

import static java.util.Arrays.asList;

/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 *
 * The main class of the plugin.
 */
public class RTechnicalDebtPlugin implements Plugin {

  /**
   * Property to be associated with suffixes of R files
   */
  public static final String PROPERTY_FILE_SUFFIXES = "sonar.r.file.suffixes";

  /**
   * Property to be associated with the metrics file, that is produced by the RScanner.
   */
  public static final String PROPERTY_METRICS_FILE = "sonar.r.tdebt.output";

  /**
   * Filename of the metrics file produced by the scanner.
   */
  private static final String FILENAME = "metrics.json";

  /**
   * Definition of the R Language for the TD plugin.
   * @param context SonarQube Context.
   */
  @Override
  public void define(Context context) {

    context.addExtensions(RLanguageDefinition.class, RQualityProfile.class);

    context.addExtensions(RMetrics.class, RMetricsSensor.class);

    // Adding all cumulative metrics to the SonarQube context.
    context.addExtensions(ComputeAverageLOC.class,ComputeAverageNPM.class,ComputeAverageNPF.class,
            ComputeAverageNMC.class,ComputeAverageNMCI.class,ComputeAverageNMCE.class,ComputeAverageWMC.class,
            ComputeAverageRFC.class, ComputeAverageNPRIF.class,ComputeAverageNPRIM.class,
            ComputeAverageAMC.class,ComputeAverageDAM.class);

    // Adding R-Language suffixes to the plugin
    // Defining the RScanner's output file in the SonarQube context.
    context.addExtensions(asList(
            PropertyDefinition.builder(PROPERTY_FILE_SUFFIXES)
                    .name("Suffixes for R")
                    .description("Comma-separated list of suffixes for R")
                    .category("R")
                    .defaultValue(".R")
                    .multiValues(true)
                    .build(),
            PropertyDefinition.builder(PROPERTY_METRICS_FILE)
                    .name("R Technical Debt script Output Filename")
                    .description("Path and filename to R Technical Debt script output in JSON format")
                    .category("R")
                    .defaultValue(FILENAME)
                    .build()
    ));
  }
}
