
SONARSCANNERDIR="$HOME/sonar-scanner"


PWD="$(PWD)"
PROJECTPATH="$1"
echo "Your SonarQube Scanner is installed in: $SONARSCANNERDIR"
echo "Please change this in the script, if this is incorrect."
echo "The current working directory: $PROJECTPATH and contains the following R files:"
echo "$(find . -type f -name "${PROJECTPATH}/*.R")"

echo "Running the R-Scanner for R files in $PROJECTPATH"
echo "${PROJECTPATH}/*.R"

python3 RScanner2.py "${PROJECTPATH}/*.R"

echo "Running Sonar-Scanner now."
# "${SONARSCANNERDIR}/bin/sonar-scanner" "-Dsonar.login=admin -Dsonar.password=admin22"
