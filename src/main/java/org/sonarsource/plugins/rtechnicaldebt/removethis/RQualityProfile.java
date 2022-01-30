package org.sonarsource.plugins.rtechnicaldebt.removethis;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.plugins.rtechnicaldebt.languages.R;

import static org.sonarsource.plugins.rtechnicaldebt.removethis.rules.RRulesDefinition.REPO_KEY;

/**
 * Default, BuiltIn Quality Profile for the projects having files of the language "foo"
 */
public final class RQualityProfile implements BuiltInQualityProfilesDefinition {

  // TODO - Do we need this? For LintR not for metrics??
  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("R Technical Debt Rules and Metrics", R.KEY);
    profile.setDefault(true);

    NewBuiltInActiveRule rule1 = profile.activateRule(REPO_KEY, "R1");
    rule1.overrideSeverity("BLOCKER");
    NewBuiltInActiveRule rule2 = profile.activateRule(REPO_KEY, "R2");
    rule2.overrideSeverity("MAJOR");
    NewBuiltInActiveRule rule3 = profile.activateRule(REPO_KEY, "R3");
    rule3.overrideSeverity("CRITICAL");

    profile.done();
  }

}
