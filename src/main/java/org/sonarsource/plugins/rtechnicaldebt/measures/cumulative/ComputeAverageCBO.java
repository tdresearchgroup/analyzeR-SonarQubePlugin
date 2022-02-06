/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.measures.cumulative;

import org.sonar.api.ce.measure.Component;

import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.rtechnicaldebt.measures.RMetrics.COUPLING_BETWEEN_OBJECTS;

public class ComputeAverageCBO implements MeasureComputer {

  @Override
  public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
    return def.newDefinitionBuilder()
      .setOutputMetrics(COUPLING_BETWEEN_OBJECTS.key())
      .build();
  }

  @Override
  public void compute(MeasureComputerContext context) {
    // measure is already defined on files by {@link SetSizeOnFilesSensor}
    // in scanner stack
    if (context.getComponent().getType() != Component.Type.FILE) {
      double sum = 0;
      int count = 0;
      for (Measure child : context.getChildrenMeasures(COUPLING_BETWEEN_OBJECTS.key())) {
        sum += child.getIntValue();
        count++;
      }
      double average = count == 0 ? 0 : sum / count;
      context.addMeasure(COUPLING_BETWEEN_OBJECTS.key(),(float) average);
    }
  }
}
