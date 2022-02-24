package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 *
 * The Quality profile is not used to its fullest in this application, as there are no explicit rules, that are set up
 * to raise issues.
 */
public final class RQualityProfile implements BuiltInQualityProfilesDefinition {
  /**
   * Quality Profile for the R language, using a builtin QualityProfile, as a placeholder with a name and the Key for
   * the R Language
   * @see RLanguageDefinition
   * @param context SonarQube context. The Quality Profile gets added to this context.
   */
  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("R Technical Debt Rules and Metrics", RLanguageDefinition.KEY);
    profile.setDefault(true);
    profile.done();
  }

}
