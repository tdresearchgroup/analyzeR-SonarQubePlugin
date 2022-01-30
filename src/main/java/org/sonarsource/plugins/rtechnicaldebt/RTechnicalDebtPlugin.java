
/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonarsource.plugins.rtechnicaldebt.languages.R;
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

    context.addExtensions(R.class, RQualityProfile.class);

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
    // Adding R option on left side of admin page

    context.addExtensions(asList(
            PropertyDefinition.builder(PROPERTY_FILE_SUFFIXES)
                    .name("Suffixes R")
                    .description("Comma-separated list of suffixes for R language")
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
