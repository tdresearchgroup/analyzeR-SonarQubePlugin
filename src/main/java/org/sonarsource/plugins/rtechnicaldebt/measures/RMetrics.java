package org.sonarsource.plugins.rtechnicaldebt.measures;

import java.util.List;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import static java.util.Arrays.asList;

/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 *
 * Definition of all metrics for R.
 * At present, these are all relatively bare-bones. More detail can be added to these metrics as we understand these
 * measures better.
 */
public class RMetrics implements Metrics {

  /**
   * Set up 5 Categories, known in SonarQube as Domains.
   * Given conspicuous names to help separate them from the default SonarQube metrics.
   */
  public static String CATEGORY_SIZE = "R Size Measures";
  public static String CATEGORY_COMPLEXITY = "R Complexity Measures";
  public static String CATEGORY_COUPLING = "R Coupling Measures";
  public static String CATEGORY_COHESION = "R Cohesion Measures";
  public static String CATEGORY_ENCAPSULATION = "R Encapsulation Measures";

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

  public static final String NUMBER_PUBLIC_FIELDS_KEY = "NPF";
  public static final Metric<Integer> NUMBER_PUBLIC_FIELDS = new Metric.Builder(NUMBER_PUBLIC_FIELDS_KEY, "Number of Public Fields", Metric.ValueType.INT)
          .setDescription("Number of public fields in an application or module")
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
  public static final Metric<Double> AVERAGE_METHOD_COMPLEXITY = new Metric.Builder(AVERAGE_METHOD_COMPLEXITY_KEY,"Average Method Complexity", Metric.ValueType.FLOAT)
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
  public static final Metric<Double> COUPLING_BETWEEN_OBJECTS = new Metric.Builder(COUPLING_BETWEEN_OBJECTS_KEY,"Coupling Between Object Classes", Metric.ValueType.FLOAT)
          .setDescription("Number of other modules or packages that an application or module is coupled too")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String AFFERENT_COUPLING_KEY = "CA";
  public static final Metric<Double> AFFERENT_COUPLING = new Metric.Builder(AFFERENT_COUPLING_KEY,"Afferent Coupling", Metric.ValueType.FLOAT)
          .setDescription("Measure of how many other applications use the specific application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String EFFERENT_COUPLING_KEY = "CE";
  public static final Metric<Double> EFFERENT_COUPLING = new Metric.Builder(EFFERENT_COUPLING_KEY,"Efferent Coupling", Metric.ValueType.FLOAT)
          .setDescription("Measure of how many other modules or packages are used by the specific application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String MARTINS_INSTABILITY_KEY = "MI";
  public static final Metric<Double> MARTINS_INSTABILITY = new Metric.Builder(MARTINS_INSTABILITY_KEY,"Martin's Instability Measure", Metric.ValueType.FLOAT)
          .setDescription("Ce/(Ce+Ca)")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  public static final String LACK_COHESION_METHODS_KEY = "LCOM";
  public static final Metric<Double> LACK_COHESION_METHODS = new Metric.Builder(LACK_COHESION_METHODS_KEY,"Lack of Cohesion in Methods", Metric.ValueType.FLOAT)
          .setDescription("Difference between the number of function pairs without and with common non static fields")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COHESION)
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

  public static final String DATA_ACCESS_METRICS_KEY = "DAM";
  public static final Metric<Double> DATA_ACCESS_METRICS = new Metric.Builder(DATA_ACCESS_METRICS_KEY,"Data Access Metrics", Metric.ValueType.FLOAT)
          .setDescription("Ratio of the number of private fields to total number of fields")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_ENCAPSULATION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Returns all the defined metrics as a list.
   * @return List of Metrics
   */
  @Override
  public List<Metric> getMetrics() {
    return asList(LINES_OF_CODE ,NUMBER_PUBLIC_METHODS , NUMBER_PUBLIC_FIELDS , NUMBER_METHOD_CALLS ,  NUMBER_METHOD_CALLS_INTERNAL , NUMBER_METHOD_CALLS_EXTERNAL ,WEIGHTED_METHODS_PER_CLASS ,
            AVERAGE_METHOD_COMPLEXITY , RESPONSE_FOR_CLASS ,COUPLING_BETWEEN_OBJECTS ,AFFERENT_COUPLING , EFFERENT_COUPLING , MARTINS_INSTABILITY , LACK_COHESION_METHODS, DATA_ACCESS_METRICS , NUMBER_PRIVATE_FIELDS ,NUMBER_PRIVATE_METHODS);
  }

  /**
   * Generic toString method to aid in troubleshooting
   * @return A string representation of the R Metrics.
   */
  @Override
  public String toString() {
    return super.toString();
  }
}
