/**
 * @author Pranav Chandramouli, University of Saskatchewan
 * Supervised by Dr. Melina Vidoni and Dr. Zadia Codabux
 */

package org.sonarsource.plugins.rtechnicaldebt;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;
import org.sonarsource.plugins.rtechnicaldebt.languages.RLanguageDefinition;

public class RRulesDefinition implements RulesDefinition {

  private static final String PATH_TO_RULES_XML = "/example/r-rules.xml";

  protected static final String KEY = "rtechdebt";
  protected static final String NAME = "RTechDebt";

  public static final String REPO_KEY = RLanguageDefinition.KEY + "-" + KEY;
  protected static final String REPO_NAME = RLanguageDefinition.KEY + "-" + NAME;

  protected String rulesDefinitionFilePath() {
    return PATH_TO_RULES_XML;
  }

  private void defineRulesForLanguage(Context context, String repositoryKey, String repositoryName, String languageKey) {
    NewRepository repository = context.createRepository(repositoryKey, languageKey).setName(repositoryName);

    InputStream rulesXml = this.getClass().getResourceAsStream(rulesDefinitionFilePath());
    if (rulesXml != null) {
      RulesDefinitionXmlLoader rulesLoader = new RulesDefinitionXmlLoader();
      rulesLoader.load(repository, rulesXml, StandardCharsets.UTF_8.name());
    }
    repository.done();
  }

  @Override
  public void define(Context context) {
    defineRulesForLanguage(context, REPO_KEY, REPO_NAME, RLanguageDefinition.KEY);
  }

}
