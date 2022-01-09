/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2020 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.plugins.RTechnicalDebtPlugin;

import org.sonar.api.Plugin;
import org.sonarsource.plugins.RTechnicalDebtPlugin.hooks.PostJobInScanner;
import org.sonarsource.plugins.RTechnicalDebtPlugin.hooks.DisplayQualityGateStatus;
import org.sonarsource.plugins.RTechnicalDebtPlugin.languages.RLanguage;
import org.sonarsource.plugins.RTechnicalDebtPlugin.languages.RQualityProfile;
import org.sonarsource.plugins.RTechnicalDebtPlugin.measures.ComputeSizeAverage;
import org.sonarsource.plugins.RTechnicalDebtPlugin.measures.ComputeSizeRating;
import org.sonarsource.plugins.RTechnicalDebtPlugin.measures.TechnicalDebtMetrics;
import org.sonarsource.plugins.RTechnicalDebtPlugin.measures.SetSizeOnFilesSensor;
import org.sonarsource.plugins.RTechnicalDebtPlugin.rules.CreateIssuesOnJavaFilesSensor;
import org.sonarsource.plugins.RTechnicalDebtPlugin.rules.FooLintIssuesLoaderSensor;
import org.sonarsource.plugins.RTechnicalDebtPlugin.rules.FooLintRulesDefinition;
import org.sonarsource.plugins.RTechnicalDebtPlugin.rules.JavaRulesDefinition;
import org.sonarsource.plugins.RTechnicalDebtPlugin.settings.FooLanguageProperties;
import org.sonarsource.plugins.RTechnicalDebtPlugin.settings.HelloWorldProperties;
import org.sonarsource.plugins.RTechnicalDebtPlugin.settings.SayHelloFromScanner;
import org.sonarsource.plugins.RTechnicalDebtPlugin.web.MyPluginPageDefinition;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class RTechnicalDebtPlugin implements Plugin {

  @Override
  public void define(Context context) {
    // tutorial on hooks
    // http://docs.sonarqube.org/display/DEV/Adding+Hooks
    context.addExtensions(PostJobInScanner.class, DisplayQualityGateStatus.class);

    // tutorial on languages
    context.addExtensions(RLanguage.class, RQualityProfile.class);
    context.addExtensions(FooLanguageProperties.getProperties());

    // tutorial on measures
    context
      .addExtensions(TechnicalDebtMetrics.class, SetSizeOnFilesSensor.class, ComputeSizeAverage.class, ComputeSizeRating.class);

    // tutorial on rules
    context.addExtensions(JavaRulesDefinition.class, CreateIssuesOnJavaFilesSensor.class);
    context.addExtensions(FooLintRulesDefinition.class, FooLintIssuesLoaderSensor.class);

    // tutorial on settings
    context
      .addExtensions(HelloWorldProperties.getProperties())
      .addExtension(SayHelloFromScanner.class);

    // tutorial on web extensions
    context.addExtension(MyPluginPageDefinition.class);
  }
}
