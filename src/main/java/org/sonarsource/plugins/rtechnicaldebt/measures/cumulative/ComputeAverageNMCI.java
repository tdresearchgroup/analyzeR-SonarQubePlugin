/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures.cumulative;

import org.sonar.api.ce.measure.Component;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.rtechnicaldebt.measures.RMetrics.NUMBER_METHOD_CALLS_INTERNAL;

/**
 * Computes Cumulative Number of Internal Method Calls
 */
public class ComputeAverageNMCI implements MeasureComputer {

  /**
   * Initializes the NMCI Metric for Project-Wide Metrics
   * @param def Instance of a MetricsComputerDefiniion Context
   * @return The Metric Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(NUMBER_METHOD_CALLS_INTERNAL.key())
      .build();
  }

  /**
   * Computes the cumulative NMCI metric.
   * @param context MeasureComputerContext object. The cumulative metric gets added to this.
   */
  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link SetSizeOnFilesSensor}
    // in scanner stack
    if (context.getComponent().getType() != Component.Type.FILE) {
      int sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(NUMBER_METHOD_CALLS_INTERNAL.key())) {
        sum += child.getIntValue();
        count++;
      }
      int average = count == 0 ? 0 : sum / count;
      context.addMeasure(NUMBER_METHOD_CALLS_INTERNAL.key(), average);
    }
  }
}
