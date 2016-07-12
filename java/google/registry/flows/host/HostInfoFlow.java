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

package google.registry.flows.host;

import google.registry.flows.ResourceInfoFlow;
import google.registry.model.host.HostCommand;
import google.registry.model.host.HostResource;
import javax.inject.Inject;

/**
 * An EPP flow that reads a host.
 *
 * @error {@link google.registry.flows.ResourceQueryFlow.ResourceToQueryDoesNotExistException}
 */
public class HostInfoFlow extends ResourceInfoFlow<HostResource, HostCommand.Info> {
  @Inject HostInfoFlow() {}
}
