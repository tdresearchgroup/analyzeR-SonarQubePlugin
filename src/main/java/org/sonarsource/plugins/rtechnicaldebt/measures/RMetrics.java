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
package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.io.IOException;
import java.util.List;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;
import org.sonarsource.plugins.rtechnicaldebt.RMetricsParser;

import static java.util.Arrays.asList;

public class RMetrics implements Metrics {

  public static final Metric<Integer> FILENAME_SIZE = new Metric.Builder("filename_size", "Filename Size", Metric.ValueType.INT)
    .setDescription("Number of characters of file names")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(false)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> FILENAME_SIZE_RATING = new Metric.Builder("filename_size_rating", "Filename Size Rating", Metric.ValueType.RATING)
    .setDescription("Rating based on size of file names")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> LINES_OF_CODE = new Metric.Builder("lines_of_code","Lines of Code", Metric.ValueType.INT).setDescription("Number of Lines of Code.").setDescription("Rating based on size of file names")
          .setDirection(Metric.DIRECTION_BETTER)
          .setQualitative(true)
          .setDomain(CoreMetrics.DOMAIN_GENERAL)
          .create();

  @Override
  public List<Metric> getMetrics() {

    /*
    try {
      RMetricsParser x = new RMetricsParser();
      x.getMetrics().forEach(m -> System.out.println(m));
    } catch (IOException e) {
      e.printStackTrace();
    }
     */
    System.out.println(LINES_OF_CODE);
    return asList(FILENAME_SIZE, FILENAME_SIZE_RATING);
  }
}
