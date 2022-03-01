package org.sonarsource.plugins.analyzeR.measures;

import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Definition of all metrics for R.
 * At present, these are all relatively bare-bones. More detail can be added to these metrics as we understand these
 * measures better.
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public class RMetrics implements Metrics {

  /**
   * Category for Size Measures Calculated by the plugin.
   */
  public static String CATEGORY_SIZE = "R Size Measures";
  /**
   * Category for Complexity Measures Calculated by the plugin.
   */
  public static String CATEGORY_COMPLEXITY = "R Complexity Measures";
  /**
   * Category for Coupling Measures Calculated by the plugin.
   */
  public static String CATEGORY_COUPLING = "R Coupling Measures";
  /**
   * Category for Cohesion Measures Calculated by the plugin.
   */
  public static String CATEGORY_COHESION = "R Cohesion Measures";
  /**
   * Category for Encapsulation Measures Calculated by the plugin.
   */
  public static String CATEGORY_ENCAPSULATION = "R Encapsulation Measures";


  /**
   * Metric Key for Lines of code.
   */
  public static final String LINES_OF_CODE_KEY = "LOC";

  /**
   * Metric Builder for Lines of code.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> LINES_OF_CODE = new Metric.Builder(LINES_OF_CODE_KEY, "Lines of Code", Metric.ValueType.INT)
          .setDescription("Number of non-blank lines")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Number of Public Methods.
   */
  public static final String NUMBER_PUBLIC_METHODS_KEY = "NPM";

  /**
   * Metric Builder for Number of Public Methods.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_PUBLIC_METHODS = new Metric.Builder(NUMBER_PUBLIC_METHODS_KEY, "Number of Public Methods", Metric.ValueType.INT)
          .setDescription("Number of public functions in an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Number of Public Fields.
   */
  public static final String NUMBER_PUBLIC_FIELDS_KEY = "NPF";

  /**
   * Metric Builder for Number of Public Fields.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_PUBLIC_FIELDS = new Metric.Builder(NUMBER_PUBLIC_FIELDS_KEY, "Number of Public Fields", Metric.ValueType.INT)
          .setDescription("Number of public fields in an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Number of Method Calls.
   */
  public static final String NUMBER_METHOD_CALLS_KEY = "NMC";

  /**
   * Metric Builder for Number of Method Calls.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_METHOD_CALLS = new Metric.Builder(NUMBER_METHOD_CALLS_KEY,"Number of Method Calls", Metric.ValueType.INT)
          .setDescription("Number of function invocations")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Number of Internal Method Calls.
   */
  public static final String NUMBER_METHOD_CALLS_INTERNAL_KEY = "NMCI";

  /**
   * Metric Builder for Number of Internal Method Calls.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_METHOD_CALLS_INTERNAL = new Metric.Builder(NUMBER_METHOD_CALLS_INTERNAL_KEY,"Number of Method Calls - Internal", Metric.ValueType.INT)
          .setDescription("Number of function invocations of function defined in the application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Number of External Method Calls.
   */
  public static final String NUMBER_METHOD_CALLS_EXTERNAL_KEY = "NMCE";

  /**
   * Metric Builder for Number of External Method Calls.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_METHOD_CALLS_EXTERNAL = new Metric.Builder(NUMBER_METHOD_CALLS_EXTERNAL_KEY,"Number of Method Calls - External", Metric.ValueType.INT)
          .setDescription("Number of function invocations of function defined in other modules or packages")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_SIZE)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Weighted Methods Per Class.
   */
  public static final String WEIGHTED_METHODS_PER_CLASS_KEY = "WMC";

  /**
   * Metric Builder for Weighted Methods Per Class.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> WEIGHTED_METHODS_PER_CLASS = new Metric.Builder(WEIGHTED_METHODS_PER_CLASS_KEY,"Weighted Methods per Class", Metric.ValueType.INT)
          .setDescription("Sum of the Cyclomatic Complexity of all function in the application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COMPLEXITY)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Average Method Complexity.
   */
  public static final String AVERAGE_METHOD_COMPLEXITY_KEY = "AMC";

  /**
   * Metric Builder for Average Method Complexity.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Double> AVERAGE_METHOD_COMPLEXITY = new Metric.Builder(AVERAGE_METHOD_COMPLEXITY_KEY,"Average Method Complexity", Metric.ValueType.FLOAT)
          .setDescription("Average of the Cyclomatic Complexity of all functions in the application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COMPLEXITY)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Response For Class.
   */
  public static final String RESPONSE_FOR_CLASS_KEY = "RFC";

  /**
   * Metric Builder for Response For Class.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> RESPONSE_FOR_CLASS = new Metric.Builder(RESPONSE_FOR_CLASS_KEY,"Response For a Class", Metric.ValueType.INT)
          .setDescription("Number of functions that response to a message from the application or module itself")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COMPLEXITY)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Coupling Between Objects.
   */
  public static final String COUPLING_BETWEEN_OBJECTS_KEY = "CBO";

  /**
   * Metric Builder for Coupling Between Objects.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Double> COUPLING_BETWEEN_OBJECTS = new Metric.Builder(COUPLING_BETWEEN_OBJECTS_KEY,"Coupling Between Object Classes", Metric.ValueType.FLOAT)
          .setDescription("Number of other modules or packages that an application or module is coupled too")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Afferent Coupling.
   */
  public static final String AFFERENT_COUPLING_KEY = "CA";

  /**
   * Metric Builder for Afferent Coupling.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Double> AFFERENT_COUPLING = new Metric.Builder(AFFERENT_COUPLING_KEY,"Afferent Coupling", Metric.ValueType.FLOAT)
          .setDescription("Measure of how many other applications use the specific application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Efferent Coupling.
   */
  public static final String EFFERENT_COUPLING_KEY = "CE";

  /**
   * Metric Builder for Efferent Coupling.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Double> EFFERENT_COUPLING = new Metric.Builder(EFFERENT_COUPLING_KEY,"Efferent Coupling", Metric.ValueType.FLOAT)
          .setDescription("Measure of how many other modules or packages are used by the specific application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Martin's Instability Measure
   */
  public static final String MARTINS_INSTABILITY_KEY = "MI";

  /**
   * Metric Builder for Martin's Instability Measure.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Double> MARTINS_INSTABILITY = new Metric.Builder(MARTINS_INSTABILITY_KEY,"Martin's Instability Measure", Metric.ValueType.FLOAT)
          .setDescription("Ce/(Ce+Ca)")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COUPLING)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Lack of Cohesion among Methods.
   */
  public static final String LACK_COHESION_METHODS_KEY = "LCOM";

  /**
   * Metric Builder for Lack of Cohesion among Methods.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Double> LACK_COHESION_METHODS = new Metric.Builder(LACK_COHESION_METHODS_KEY,"Lack of Cohesion in Methods", Metric.ValueType.FLOAT)
          .setDescription("Difference between the number of function pairs without and with common non static fields")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_COHESION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Number of Private Fields.
   */
  public static final String NUMBER_PRIVATE_FIELDS_KEY = "NPRIF";

  /**
   * Metric Builder for Number of Private Fields.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_PRIVATE_FIELDS = new Metric.Builder(NUMBER_PRIVATE_FIELDS_KEY,"Number of Private Fields", Metric.ValueType.INT)
          .setDescription("Number of private fields of an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_ENCAPSULATION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();
  /**
   * Metric Key for Number of Private Methods.
   */
  public static final String NUMBER_PRIVATE_METHODS_KEY = "NPRIM";

  /**
   * Metric Builder for Number of Private Methods.
   * Additional details for this metric can be added here.
   */
  public static final Metric<Integer> NUMBER_PRIVATE_METHODS = new Metric.Builder(NUMBER_PRIVATE_METHODS_KEY,"Number of Private Methods", Metric.ValueType.INT)
          .setDescription("Number of private functions of an application or module")
          .setDirection(Metric.DIRECTION_WORST)
          .setQualitative(false)
          .setDomain(CATEGORY_ENCAPSULATION)
          .setBestValue(0.0)
          .setOptimizedBestValue(true)
          .create();

  /**
   * Metric Key for Data Access Metrics.
   */
  public static final String DATA_ACCESS_METRICS_KEY = "DAM";

  /**
   * Metric Builder for Data Access Metrics.
   * Additional details for this metric can be added here.
   */
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
