[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

![GitHub Issues](https://img.shields.io/github/issues/tdresearchgroup/R-Plugin)

![GitHub PRs](https://img.shields.io/github/issues-pr/tdresearchgroup/R-Plugin)



# analyzeR

**analyzeR** is an open-source SonarQube plugin for automated software analysis and measurement of R packages written using one (or a combination of) the following OO implementations in R: _S3_, _S4_, and/or _R6_. It is built on the [SonarQube Plugin Example](https://github.com/SonarSource/sonar-custom-plugin-example)

The resulting measurements are presented through the SonarQube interface to improve readability and comparisons with other outputs.

Overall, **analyzeR** offers the following functionalities:

- Enables automated static code analyses of OO-structured R packages, through a process that can be incorporated into a CI setup, following current industry practices[^1].

- Calculates metrics on a per-file, per-module, per-class, and per-project basis. Note that, in OO R programming, projects/modules are represented as packages.

- Capabilities to analyze R packages written with S3, S4 or R6 OO systems, with scope to extend the plugin to support additional OO systems, like `proto` and `R.oo`, or newer systems that may become available in the future

- The plugin's implementation combines R, Python and Java. The original parsing of R packages is done through the _RParser_ (a script written in R). The code scanning and measurement is written in Python and favour extensibility to add additional metrics. The _RPlugin_ is written in Java. Documentation for all three parts is available here.

- Measurement results are available through the SonarQube interface, respecting the placement and structure provided in the plugins used to examine other languages. This allows distinguishing between comparable and custom measurements, aggregation, and visualization of results.


## Current Metrics

In version 1.0.0, support is only provided for S3, S4, and R6, by calculating traditional OO metrics[^2]. At this point, **analyzeR** does not offer any R-specific metric, as the priority was to assemble a plugin compatible with current tools that could be easily extended.

The following metrics are considered:


| Acron. | Metric                      | Description                                                                                                 |
|--------|-----------------------------|-------------------------------------------------------------------------------------------------------------|
| LOC    | Lines of Code               | Number of non-blank lines                                                                                   |
| NPM    | # of Public Methods         | Number of public functions in an application                                                                |
| NOF    | # of Fields                 | Number of public fields in an application                                                                   |
| NMC    | # of Method Calls           | Number of function invocations                                                                              |
| NMCI   | # of Internal Method Calls  | Number of function invocations of function defined in the application                                       |
| NMCE   | # of External Method Calls  | Number of function invocations of function defined in other modules or packages                             |
| WMC    | Weighted Methods per Class  | Sum of the CycloComp of all functions in the application                                                    |
| AMC    | Average Method Complexity   | Average of the CycloComp of all functions in the application                                                |
| RFC    | Response For a Class        | Number of functions that respond to the application itself                                                  |
| CBO    | Between Object Classes      | How many other modules an application (or module) is coupled to                                             |
| CA     | Afferent Coupling           | How many other applications use the specific app./module                                                    |
| CE     | Efferent Coupling           | How many other modules are used by the specific app./module                                                 |
| MI     | Martin's Instability        | Indicates the necessity of performing modifications in an entity due to updates in other software entities. |
| LCOM   | Lack of Cohesion in Methods | Difference between the number of function pairs without and with common non static fields                   |
| DAM    | Data Access Metrics         | Ratio of the number of private fields to total number of fields                                             |
| NPRIF  | Number of Private Fields    | Number of private fields of an application or module                                                        |
| NPRIM  | Number of Private Methods   | Number of private functions of an application or module                                                     |



## Installation & Use

Please, refer to the current [Wiki Entry]() regarding installation guidance.





## References

[^1] [10.1109/SANER48275.2020.9054821](https://doi.org/10.1109/SANER48275.2020.9054821)
[^2] [10.1145/3472163.3472172](https://doi.org/10.1145/3472163.3472172)
