package org.sonarsource.plugins.rtechnicaldebt.measures;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

import static org.sonarsource.plugins.rtechnicaldebt.measures.RMetrics.NUMBER_PRIVATE_METHODS;

public class computeNPRIMaverage implements MeasureComputer {

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext def) {
        return def.newDefinitionBuilder()
                .setOutputMetrics(NUMBER_PRIVATE_METHODS.key())
                .build();
    }

    @Override
    public void compute(MeasureComputerContext context) {
        // measure is already defined on files by {@link SetSizeOnFilesSensor}
        // in scanner stack
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
