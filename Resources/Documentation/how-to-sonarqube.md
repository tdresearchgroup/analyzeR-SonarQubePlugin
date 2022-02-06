# Quickstart SonarQube, SonarScanner and Plug-In Guide

### SonarQube
This is a quick guide to install and run SonarQube.
1. Download SonarQube from [here](https://www.sonarqube.org/downloads/)
2. Extract the `.zip`, to a location `<SonarQube>`
3. On Windows, execute `<SonarQube>\bin\windows-x86-64\StartSonar.bat`, on macOS and Linux run `<SonarQube>/bin/<OS>/sonar.sh console`. You can also run `sonar.sh start` to start the server and `sonar.sh stop` to stop the server.
4. SonarQube will be running on [http://localhost:9000/](http://localhost:9000/) `9000` is default, but this can be changed in `sonar.properties`
5. The default credentials are: login=`admin` and password=`admin`, you will be prompted to change these, remember them.

### SonarScanner
This is a quick guide to install SonarScanner
1. Download the scanner [here](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/)
2. Extract the `.zip`, to a location `<SonarScanner>`
3. We'll run this after taking care of the plugin and dependencies


### SonarQube R Technical Debt Plugin
This is a guide to running the R Technical Debt Plugin for SonarQube

#### Dependencies
This plug-in requires R and Python to be installed locally with the following packages.

##### R
Install R
```bash
brew install r
```

Install prerequisite packages
```bash
Rscript -e 'install.packages(c("cyclocomp","xmlparsedata"))'
```

##### Python
```bash
brew install python@3.9
```
Install python packages
```bash
pip install lxml
```

#### Plug-In
The plug-in requires 3 files, [found here](../../Parsing-Scripts). Move all 3 to the project's root file.
* `RScanner.py` This is the file that generates the metrics.
* `sonar-project.properties` This is a generic example. More on this later.
* `sonar-scanner.sh` Run this bat file to run the scanner.

##### `sonar-project.properties`
Official Documentation is found [here](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/)


[create an anchor](#anchors-in-markdown)













#anchors-in-markdown