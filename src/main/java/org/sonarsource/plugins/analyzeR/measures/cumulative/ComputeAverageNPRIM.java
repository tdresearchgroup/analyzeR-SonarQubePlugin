package org.sonarsource.plugins.analyzeR.measures.cumulative;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.analyzeR.measures.RMetrics.NUMBER_PRIVATE_METHODS;

/**
 * Measure Computer to compute Cumulative Number of Private Methods
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public class ComputeAverageNPRIM implements MeasureComputer {

  /**
   * Initializes the NPRIM Metric for Project-Wide Metrics
   * @param def Instance of a MetricsComputerDefiniion Context
   * @return The Metric Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(NUMBER_PRIVATE_METHODS.key())
      .build();
  }

  /**
   * Computes the cumulative NPRIM metric.
   * @param context MeasureComputerContext object. The cumulative metric gets added to this.
   */
  @Override
  public void compute(MeasureComputerContext context) {

    if (context.getComponent().getType() != Component.Type.FILE) {
      int sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(NUMBER_PRIVATE_METHODS.key())) {
        sum += child.getIntValue();
        count++;
      }
      int average = count == 0 ? 0 : sum / count;
      context.addMeasure(NUMBER_PRIVATE_METHODS.key(), average);
    }
  }
}
