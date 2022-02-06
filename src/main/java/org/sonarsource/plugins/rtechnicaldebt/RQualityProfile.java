/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
package org.sonarsource.plugins.rtechnicaldebt;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.plugins.rtechnicaldebt.languages.RLanguageDefinition;

public final class RQualityProfile implements BuiltInQualityProfilesDefinition {
  /**
   * Quality Profile for the R language.
   * The Quality profile is not used to its fullest in this application, as there are no explicit rules.
   * @param context SonarQube context.
   */
  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("R Technical Debt Rules and Metrics", RLanguageDefinition.KEY);
    profile.setDefault(true);
    profile.done();
  }

}
