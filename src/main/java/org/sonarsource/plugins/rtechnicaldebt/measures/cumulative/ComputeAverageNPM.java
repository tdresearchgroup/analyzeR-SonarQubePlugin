package org.sonarsource.plugins.rtechnicaldebt.measures.cumulative;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.rtechnicaldebt.measures.RMetrics.NUMBER_PUBLIC_METHODS;

/**
 * Measure Computer to compute Cumulative Number of Public Methods
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public class ComputeAverageNPM implements MeasureComputer {

  /**
   * Initializes the NPM Metric for Project-Wide Metrics
   * @param def Instance of a MetricsComputerDefiniion Context
   * @return The Metric Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(NUMBER_PUBLIC_METHODS.key())
      .build();
  }

  /**
   * Computes the cumulative NPM metric.
   * @param context MeasureComputerContext object. The cumulative metric gets added to this.
   */
  @Override
  public void compute(MeasureComputerContext context) {

    if (context.getComponent().getType() != Component.Type.FILE) {
      int sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(NUMBER_PUBLIC_METHODS.key())) {
        sum += child.getIntValue();
        count++;
      }
      int average = count == 0 ? 0 : sum / count;
      context.addMeasure(NUMBER_PUBLIC_METHODS.key(), average);
    }
  }
}
