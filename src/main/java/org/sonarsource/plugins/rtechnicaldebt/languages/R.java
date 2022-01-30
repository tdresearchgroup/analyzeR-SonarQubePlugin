/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt.languages;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;
import org.sonarsource.plugins.rtechnicaldebt.RPlugin;

/**
 * Definition of R for the plugin.
 */
public final class R extends AbstractLanguage {

  public static final String NAME = "R";
  public static final String KEY = "r";

  private final Configuration config;

  public R(Configuration config) {
    super(KEY, NAME);
    this.config = config;
  }

  @Override
  public String[] getFileSuffixes() {
    return config.getStringArray(RPlugin.PROPERTY_FILE_SUFFIXES);
  }

}
