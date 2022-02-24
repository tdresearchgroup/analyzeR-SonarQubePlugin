# SonarQube and SonarScanner Set-Up and Start Up

### SonarQube
This is a quick guide to install and run SonarQube.
1. Download SonarQube from [here](https://www.sonarqube.org/downloads/)
2. Extract the `.zip`, to a location, which we'll call `<SonarQube>`
3. On Windows, execute `<SonarQube>\bin\windows-x86-64\StartSonar.bat`, on macOS and Linux run `<SonarQube>/bin/<OS>/sonar.sh console`. You can also run `sonar.sh start` to start the server and `sonar.sh stop` to stop the server.
4. SonarQube will be running on [http://localhost:9000/](http://localhost:9000/) `9000` is default, but this can be changed in `sonar.properties`
5. The default credentials are: login=`admin` and password=`admin`, you will be prompted to change these, remember them.

### SonarScanner
This is a quick guide to install SonarScanner. This is required when there is no scanner for the build system. This is the case for R, so this is required.
1. Download the scanner [here](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner/)
2. Extract the `.zip`, to a location `<SonarScanner>`
3. We'll run this after taking care of the plugin and dependencies
