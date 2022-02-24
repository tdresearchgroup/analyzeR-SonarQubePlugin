# SonarQube R Technical Debt Plugin

This is a guide to running the R Technical Debt Plugin for SonarQube

Installation Instructions for SonarQube are [here](sonarqube-setup.md)

### Dependencies
This plug-in requires R and Python to be installed locally with the following packages.

##### R
1. Install R, can be downloaded [here.](https://www.r-project.org)

2. Install prerequisite R packages
```bash
Rscript -e 'install.packages(c("cyclocomp","xmlparsedata"))'
```

##### Python
1. Install python, can be downloaded [here.](https://www.python.org/downloads/)

2. Install prerequisite python packages
```bash
pip install lxml
```

#### Plug-In
Let ``<Project>`` be the directory with the source files that need to be analysed.

Move the plugin `.jar` file to `<SonarQube>/extensions/plugins` and restart the server.

Begin by creating a file called: `sonar-project.properties`
The plug-in requires 3 files, [found here](../../Parsing-Scripts). Move all 3 to the project's root file.
* `RScanner.py` This is the file that generates the metrics.
* `sonar-project.properties` This is a generic example. More on this later.
* `sonar-scanner.sh` Run this bat file to run the scanner.



