/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2020 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonarsource.plugins.rtechnicaldebt.languages.R;
import org.sonarsource.plugins.rtechnicaldebt.languages.RQualityProfile;
import org.sonarsource.plugins.rtechnicaldebt.measures.*;
import org.sonarsource.plugins.rtechnicaldebt.measures.cumulative.*;
import org.sonarsource.plugins.rtechnicaldebt.rules.RRulesDefinition;
import org.sonarsource.plugins.rtechnicaldebt.web.RPluginPageDefinition;

import static java.util.Arrays.asList;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class RPlugin implements Plugin {

  public static final String PROPERTY_FILE_SUFFIXES = "sonar.r.file.suffixes";

  public static final String PROPERTY_METRICS_FILE = "sonar.r.tdebt.output";

  private static final String FILENAME = "metrics.json";

  @Override
  public void define(Context context) {

    // tutorial on languages
    context.addExtensions(R.class, RQualityProfile.class);

    // tutorial on measures
    context.addExtensions(RMetrics.class, RMetricsSensor.class);

    // Average Metric for all metrics
    context.addExtensions(ComputeAverageLOC.class,ComputeAverageNPM.class,ComputeAverageNPF.class,ComputeAverageNSTAF.class,ComputeAverageNMC.class,ComputeAverageNMCI.class,ComputeAverageNMCE.class,ComputeAverageWMC.class,ComputeAverageAMC.class,ComputeAverageRFC.class,ComputeAverageCBO.class,ComputeAverageCA.class,ComputeAverageCE.class,ComputeAverageMI.class,ComputeAverageLCOM.class, ComputeAverageCAM.class,ComputeAverageNPRIF.class,ComputeAverageNPRIM.class,ComputeAverageDAM.class);


    // TODO - Might need to clean this up
    // tutorial on rules
    context.addExtension(RRulesDefinition.class);

    // tutorial on web extensions
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
