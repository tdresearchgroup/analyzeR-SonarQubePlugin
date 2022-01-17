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

import static java.util.Arrays.asList;

public class RMetrics implements Metrics {

  public static final Metric<Integer> FILENAME_SIZE = new Metric.Builder("filename_size", "R TD Filename Size", Metric.ValueType.INT)
    .setDescription("Number of characters of file names")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(false)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();

  public static final Metric<Integer> FILENAME_SIZE_RATING = new Metric.Builder("filename_size_rating", "R TD Filename Size Rating", Metric.ValueType.RATING)
    .setDescription("Rating based on size of file names")
    .setDirection(Metric.DIRECTION_BETTER)
    .setQualitative(true)
    .setDomain(CoreMetrics.DOMAIN_GENERAL)
    .create();


  // Metric Categories
  public static String CATEGORY_SIZE = "R Size";
  public static String CATEGORY_COMPLEXITY = "R Complexity";
  public static String CATEGORY_COUPLING = "R Coupling Measures";
  public static String CATEGORY_COHESION = "R Cohesion";
  public static String CATEGORY_ENCAPSULATION = "R Encapsulation";

  public static final String LINES_OF_CODE_KEY = "LOC";
  public static final Metric<Integer> LINES_OF_CODE = new Metric.Builder(LINES_OF_CODE_KEY, "Lines of Code", Metric.ValueType.INT)
          .setDescription("Number of non-blank lines")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_PUBLIC_METHODS_KEY = "NPM";
  public static final Metric<Integer> NUMBER_PUBLIC_METHODS = new Metric.Builder(NUMBER_PUBLIC_METHODS_KEY, "Number of Public Methods", Metric.ValueType.INT)
          .setDescription("Number of public functions in an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_STATIC_FIELDS_KEY = "NSTAF";
  public static final Metric<Integer> NUMBER_STATIC_FIELDS = new Metric.Builder(NUMBER_STATIC_FIELDS_KEY, "Number of Static Fields", Metric.ValueType.INT)
          .setDescription("Number of static fields in an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_METHOD_CALLS_KEY = "NMC";
  public static final Metric<Integer> NUMBER_METHOD_CALLS = new Metric.Builder(NUMBER_METHOD_CALLS_KEY,"Number of Method Calls", Metric.ValueType.INT)
          .setDescription("Number of function invocations")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_METHOD_CALLS_INTERNAL_KEY = "NMCI";
  public static final Metric<Integer> NUMBER_METHOD_CALLS_INTERNAL = new Metric.Builder(NUMBER_METHOD_CALLS_INTERNAL_KEY,"Number of Method Calls - Internal", Metric.ValueType.INT)
          .setDescription("Number of function invocations of function defined in the application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_METHOD_CALLS_EXTERNAL_KEY = "NMCE";
  public static final Metric<Integer> NUMBER_METHOD_CALLS_EXTERNAL = new Metric.Builder(NUMBER_METHOD_CALLS_EXTERNAL_KEY,"Number of Method Calls - External", Metric.ValueType.INT)
          .setDescription("Number of function invocations of function defined in other modules or packages")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String WEIGHTED_METHODS_PER_CLASS_KEY = "WMC";
  public static final Metric<Integer> WEIGHTED_METHODS_PER_CLASS = new Metric.Builder(WEIGHTED_METHODS_PER_CLASS_KEY,"Weighted Methods per Class", Metric.ValueType.INT)
          .setDescription("Sum of the Cyclomatic Complexity of all function in the application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COMPLEXITY)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String AVERAGE_METHOD_COMPLEXITY_KEY = "AMC";
  public static final Metric<Float> AVERAGE_METHOD_COMPLEXITY = new Metric.Builder(AVERAGE_METHOD_COMPLEXITY_KEY,"Average Method Complexity", Metric.ValueType.FLOAT)
          .setDescription("Average of the Cyclomatic Complexity of all functions in the application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COMPLEXITY)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String RESPONSE_FOR_CLASS_KEY = "RFC";
  public static final Metric<Integer> RESPONSE_FOR_CLASS = new Metric.Builder(RESPONSE_FOR_CLASS_KEY,"Response For a Class", Metric.ValueType.INT)
          .setDescription("Number of functions that response to a message from the application or module itself")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COMPLEXITY)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String COUPLING_BETWEEN_OBJECTS_KEY = "CBO";
  public static final Metric<Integer> COUPLING_BETWEEN_OBJECTS = new Metric.Builder(COUPLING_BETWEEN_OBJECTS_KEY,"Coupling Between Object Classes", Metric.ValueType.INT)
          .setDescription("Number of other modules or packages that an application or module is coupled too")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String AFFERENT_COUPLING_KEY = "CA";
  public static final Metric<Integer> AFFERENT_COUPLING = new Metric.Builder(AFFERENT_COUPLING_KEY,"Afferent Coupling", Metric.ValueType.INT)
          .setDescription("Measure of how many other applications use the specific application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String EFFERENT_COUPLING_KEY = "CE";
  public static final Metric<Integer> EFFERENT_COUPLING = new Metric.Builder(EFFERENT_COUPLING_KEY,"Efferent Coupling", Metric.ValueType.INT)
          .setDescription("Measure of how many other modules or packages are used by the specific application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String MARTINS_INSTABILITY_KEY = "MI";
  public static final Metric<Float> MARTINS_INSTABILITY = new Metric.Builder(MARTINS_INSTABILITY_KEY,"Martin's Instability Measure", Metric.ValueType.FLOAT)
          .setDescription("Ce/(Ce+Ca)")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String LACK_COHESION_METHODS_KEY = "LCOM";
  public static final Metric<Integer> LACK_OF_COHESION_IN_METHODS = new Metric.Builder(LACK_COHESION_METHODS_KEY,"Lack of Cohesion in Methods", Metric.ValueType.INT)
          .setDescription("Difference between the number of function pairs without and with common non static fields")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COHESION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String COHESION_AMONG_METHODS_KEY = "CAM";
  public static final Metric<Integer> COHESION_AMONG_METHODS = new Metric.Builder(COHESION_AMONG_METHODS_KEY,"Cohesion Among Methods", Metric.ValueType.INT)
          .setDescription("Represents the relatedness among functions of an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COHESION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String DATA_ACCESS_METRICS_KEY = "DAM";
  public static final Metric<Float> DATA_ACCESS_METRICS = new Metric.Builder(DATA_ACCESS_METRICS_KEY,"Data Access Metrics", Metric.ValueType.INT)
          .setDescription("Ratio of the number of private fields to total number of fields")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_ENCAPSULATION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_PRIVATE_FIELDS_KEY = "NPRIF";
  public static final Metric<Integer> NUMBER_PRIVATE_FIELDS = new Metric.Builder(NUMBER_PRIVATE_FIELDS_KEY,"Number of Private Fields", Metric.ValueType.INT)
          .setDescription("Number of private fields of an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_ENCAPSULATION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String NUMBER_PRIVATE_METHODS_KEY = "NPRIM";
  public static final Metric<Integer> NUMBER_PRIVATE_METHODS = new Metric.Builder(NUMBER_PRIVATE_METHODS_KEY,"Number of Private Methods", Metric.ValueType.INT)
          .setDescription("Number of private functions of an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_ENCAPSULATION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();



  @Override
  public List<Metric> getMetrics() {
    //return asList(FILENAME_SIZE, FILENAME_SIZE_RATING,RTD_LOC_SIZE,RTD_LOC_SIZE_RATING,RTD_NMC_SIZE,RTD_NMCI_SIZE);
    return asList(LINES_OF_CODE ,NUMBER_PUBLIC_METHODS , NUMBER_STATIC_FIELDS , NUMBER_METHOD_CALLS ,  NUMBER_METHOD_CALLS_INTERNAL , NUMBER_METHOD_CALLS_EXTERNAL ,WEIGHTED_METHODS_PER_CLASS ,
            AVERAGE_METHOD_COMPLEXITY , RESPONSE_FOR_CLASS ,COUPLING_BETWEEN_OBJECTS ,AFFERENT_COUPLING , EFFERENT_COUPLING , MARTINS_INSTABILITY ,LACK_OF_COHESION_IN_METHODS , COHESION_AMONG_METHODS , DATA_ACCESS_METRICS , NUMBER_PRIVATE_FIELDS ,NUMBER_PRIVATE_METHODS);
  }
}
