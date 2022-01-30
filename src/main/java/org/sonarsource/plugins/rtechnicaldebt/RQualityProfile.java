/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.plugins.rtechnicaldebt.languages.RLanguageDefinition;

import static org.sonarsource.plugins.rtechnicaldebt.removethis.rules.RRulesDefinition.REPO_KEY;

public final class RQualityProfile implements BuiltInQualityProfilesDefinition {

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("R Technical Debt Rules and Metrics", RLanguageDefinition.KEY);
    profile.setDefault(true);
    profile.done();
  }

}
