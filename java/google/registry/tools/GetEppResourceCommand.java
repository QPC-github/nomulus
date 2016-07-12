// Copyright 2016 The Domain Registry Authors. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package google.registry.tools;

import static com.google.common.base.Preconditions.checkArgument;
import static google.registry.model.EppResourceUtils.loadByUniqueId;
import static org.joda.time.DateTimeZone.UTC;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.googlecode.objectify.Key;
import google.registry.model.EppResource;
import google.registry.tools.Command.RemoteApiCommand;
import google.registry.util.TypeUtils.TypeInstantiator;
import org.joda.time.DateTime;

/**
 * Abstract command to show a resource.
 *
 * @param <R> {@link EppResource} subclass.
 */
@Parameters(separators = " =")
abstract class GetEppResourceCommand<R extends EppResource>
    implements RemoteApiCommand {

  private final DateTime now = DateTime.now(UTC);

  private Class<R> clazz = new TypeInstantiator<R>(getClass()){}.getExactType();

  @Parameter(
      names = "--read_timestamp",
      description = "Timestamp to use when reading. May not be in the past.")
  protected DateTime readTimestamp = now;

  @Parameter(
      names = "--expand",
      description = "Fully expand the requested resource. NOTE: Output may be lengthy.")
  boolean expand;

  /** Resolve any parameters into ids for loadResource. */
  abstract void processParameters();

  /** 
   * Load a resource by ID and output. Append the websafe key to the output for use in e.g.
   * manual mapreduce calls.
   */
  void printResource(String uniqueId) {
    R resource = loadByUniqueId(clazz, uniqueId, readTimestamp);
    System.out.println(resource != null
        ? String.format("%s\n\nWebsafe key: %s", 
            expand ? resource.toHydratedString() : resource,
            Key.create(resource).getString())
        : String.format(
            "%s '%s' does not exist or is deleted\n",
            clazz.getSimpleName().replace("Resource", ""),
            uniqueId));
  }

  @Override
  public void run() {
    checkArgument(!readTimestamp.isBefore(now), "--read_timestamp may not be in the past");
    processParameters();
  }
}
