package org.sonarsource.plugins.analyzeR.measures.cumulative;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.analyzeR.measures.RMetrics.AVERAGE_METHOD_COMPLEXITY;

/**
 * Measure Computer to compute Cumulative Average Method Complexity
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public class ComputeAverageAMC implements MeasureComputer {

  /**
   * Initializes the AMC Metric for Project-Wide Metrics
   * @param def Instance of a MetricsComputerDefiniion Context
   * @return The Metric Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(AVERAGE_METHOD_COMPLEXITY.key())
      .build();
  }

  /**
   * Computes the cumulative AMC metric.
   * @param context MeasureComputerContext object. The cumulative metric gets added to this.
   */
  @Override
  public void compute(MeasureComputerContext context) {

    if (context.getComponent().getType() != Component.Type.FILE) {
      double sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(AVERAGE_METHOD_COMPLEXITY.key())) {
        sum += child.getDoubleValue();
        count++;
      }
      double average = count == 0 ? 0 : sum / count;
      context.addMeasure(AVERAGE_METHOD_COMPLEXITY.key(), (float) average);
    }
  }
}
