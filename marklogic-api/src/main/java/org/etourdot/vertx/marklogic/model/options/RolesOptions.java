/*
 * Copyright (C) 2015 - 2016 Emmanuel Tourdot
 *
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this software.
 * If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.etourdot.vertx.marklogic.model.options;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import org.etourdot.vertx.marklogic.model.management.Role;

import static java.util.Optional.ofNullable;

@DataObject(generateConverter = true)
public class RolesOptions extends NameViewOptions {
  private Role role;

  public RolesOptions() {
    super();
  }

  public RolesOptions(RolesOptions rolesOptions) {
    super(rolesOptions);
    ofNullable(rolesOptions.getRole()).ifPresent(this::role);
  }

  public RolesOptions(JsonObject jsonObject) {
    super(jsonObject);
    ofNullable(jsonObject.getJsonObject("role")).ifPresent(o -> role = new Role(o));
  }

  public JsonObject toJson() {
    JsonObject jsonObject = super.toJson();
    ofNullable(role).ifPresent(o -> jsonObject.put("role", o.toJson()));
    return jsonObject;
  }

  /**
   * Role
   */
  public Role getRole() {
    return role;
  }

  public RolesOptions role(Role role) {
    this.role = role;
    return this;
  }

  public boolean hasRole() {
    return ofNullable(role).isPresent();
  }
}
