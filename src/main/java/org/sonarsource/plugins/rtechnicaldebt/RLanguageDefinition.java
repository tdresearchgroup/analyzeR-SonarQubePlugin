package org.sonarsource.plugins.rtechnicaldebt;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

/**
 * Definition of the R Language for the plugin.
 * Definition contains a name and a key
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */
public final class RLanguageDefinition extends AbstractLanguage {

  /**
   * The Name of the language, which is displayed in the interface.
   */
  public static final String NAME = "R";

  /**
   * The key of the language, which is used to retrieve the language. This is important as it is used to implement rules, etc
   */
  public static final String KEY = "r";

  private final Configuration config;
  /**
   * Defining the R language to have a name, and key
   * @param config SonarQube configuration object
   */
  public RLanguageDefinition(Configuration config) {
    super(KEY, NAME);
    this.config = config;
  }

  /**
   * Returns a list of suffixes for the R-Language
   * @return List of suffixes, in this case .R
   */
  @Override
  public String[] getFileSuffixes() {
    return config.getStringArray(RTechnicalDebtPlugin.PROPERTY_FILE_SUFFIXES);
  }

}
