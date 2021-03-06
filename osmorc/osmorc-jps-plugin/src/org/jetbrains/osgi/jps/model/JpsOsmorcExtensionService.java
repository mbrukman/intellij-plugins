/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.osgi.jps.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.module.JpsModule;

/**
 * @author michael.golubev
 */
public class JpsOsmorcExtensionService {
  private static final JpsOsmorcExtensionService INSTANCE = new JpsOsmorcExtensionService();

  private JpsOsmorcExtensionService() { }

  /** deprecated to be removed in IDEA 16 */
  public static JpsOsmorcExtensionService getInstance() {
    return INSTANCE;
  }

  @Nullable
  public static JpsOsmorcProjectExtension getExtension(@NotNull JpsProject project) {
    return project.getContainer().getChild(JpsOsmorcProjectExtension.ROLE);
  }

  @Nullable
  public static JpsOsmorcModuleExtension getExtension(@NotNull JpsModule module) {
    return module.getContainer().getChild(JpsOsmorcModuleExtension.ROLE);
  }
}
