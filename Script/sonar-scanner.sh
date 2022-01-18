
SONARSCANNERDIR="$HOME/sonar-scanner"


PWD="$(PWD)"
echo "Your SonarQube Scanner is installed in: $SONARSCANNERDIR"
echo "Please change this in the script, if this is incorrect."
echo "The current working directory: $PWD and contains the following R files:"
echo "$(find . -type f -name "*.R")"
echo "Running Sonar-Scanner now."

python3


"${SONARSCANNERDIR}/bin/sonar-scanner" "-Dsonar.login=admin -Dsonar.password=admin22"
