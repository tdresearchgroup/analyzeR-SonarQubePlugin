package org.sonarsource.plugins.analyzeR.measures.cumulative;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.analyzeR.measures.RMetrics.NUMBER_PRIVATE_FIELDS;

/**
 * Measure Computer to compute Cumulative Number of Private Fields
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public class ComputeAverageNPRIF implements MeasureComputer {

  /**
   * Initializes the NPRIF Metric for Project-Wide Metrics
   * @param def Instance of a MetricsComputerDefiniion Context
   * @return The Metric Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(NUMBER_PRIVATE_FIELDS.key())
      .build();
  }

  /**
   * Computes the cumulative NPRIF metric.
   * @param context MeasureComputerContext object. The cumulative metric gets added to this.
   */
  @Override
  public void compute(MeasureComputerContext context) {

    if (context.getComponent().getType() != Component.Type.FILE) {
      int sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(NUMBER_PRIVATE_FIELDS.key())) {
        sum += child.getIntValue();
        count++;
      }
      int average = count == 0 ? 0 : sum / count;
      context.addMeasure(NUMBER_PRIVATE_FIELDS.key(), average);
    }
  }
}
