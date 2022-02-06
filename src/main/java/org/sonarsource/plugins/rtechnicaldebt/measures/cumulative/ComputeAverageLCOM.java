/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures.cumulative;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.rtechnicaldebt.measures.RMetrics.LACK_COHESION_METHODS;

/**
 * Computes Cumulative Lack of Cohesion Among Methods
 */
public class ComputeAverageLCOM implements MeasureComputer {

  /**
   * Initializes the LCOM Metric for Project-Wide Metrics
   * @param def Instance of a MetricsComputerDefiniion Context
   * @return The Metric Definition
   */
  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(LACK_COHESION_METHODS.key())
      .build();
  }

  /**
   * Computes the cumulative LCOM metric.
   * @param context MeasureComputerContext object. The cumulative metric gets added to this.
   */
  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link SetSizeOnFilesSensor}
    // in scanner stack
    if (context.getComponent().getType() != Component.Type.FILE) {
      double sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(LACK_COHESION_METHODS.key())) {
        sum += child.getIntValue();
        count++;
      }
      double average = count == 0 ? 0 : sum / count;
      context.addMeasure(LACK_COHESION_METHODS.key(),(float) average);
    }
  }
}
