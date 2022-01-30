
/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonarsource.plugins.rtechnicaldebt.languages.RLanguageDefinition;
import org.sonarsource.plugins.rtechnicaldebt.removethis.RQualityProfile;
import org.sonarsource.plugins.rtechnicaldebt.measures.*;
import org.sonarsource.plugins.rtechnicaldebt.measures.cumulative.*;
import org.sonarsource.plugins.rtechnicaldebt.removethis.rules.RRulesDefinition;
import org.sonarsource.plugins.rtechnicaldebt.removethis.web.RPluginPageDefinition;

import static java.util.Arrays.asList;

public class RTechnicalDebtPlugin implements Plugin {

  public static final String PROPERTY_FILE_SUFFIXES = "sonar.r.file.suffixes";

  public static final String PROPERTY_METRICS_FILE = "sonar.r.tdebt.output";

  private static final String FILENAME = "metrics.json";

  @Override
  public void define(Context context) {

    context.addExtensions(RLanguageDefinition.class, RQualityProfile.class);

    context.addExtensions(RMetrics.class, RMetricsSensor.class);


    // TODO -> Clean this up
    // Average Metric for all metrics
    context.addExtensions(ComputeAverageLOC.class,ComputeAverageNPM.class,ComputeAverageNPF.class,
            ComputeAverageNMC.class,ComputeAverageNMCI.class,ComputeAverageNMCE.class,ComputeAverageWMC.class,
            ComputeAverageRFC.class,ComputeAverageCBO.class,ComputeAverageCA.class,ComputeAverageCE.class,
            ComputeAverageLCOM.class, ComputeAverageCAM.class,ComputeAverageNPRIF.class,ComputeAverageNPRIM.class,
            ComputeAverageAMC.class,ComputeAverageMI.class,ComputeAverageDAM.class);

    // TODO - Int <-> Double <-> FLoat issues with ComputeAverageAMC.class,ComputeAverageMI.class,ComputeAverageDAM.class

    context.addExtension(RRulesDefinition.class);

    context.addExtension(RPluginPageDefinition.class);

    // Adding Metrics File Output
    // Adding RLanguageDefinition option on left side of admin page

    context.addExtensions(asList(
            PropertyDefinition.builder(PROPERTY_FILE_SUFFIXES)
                    .name("Suffixes RLanguageDefinition")
                    .description("Comma-separated list of suffixes for RLanguageDefinition language")
                    .category("RLanguageDefinition")
                    .defaultValue(".RLanguageDefinition")
                    .multiValues(true)
                    .build(),
            PropertyDefinition.builder(PROPERTY_METRICS_FILE)
                    .name("RLanguageDefinition Technical Debt script Output Filename")
                    .description("Path and filename to RLanguageDefinition Technical Debt script output in JSON format")
                    .category("RLanguageDefinition")
                    .defaultValue(FILENAME)
                    .build()
    ));
  }
}
