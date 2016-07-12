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

package google.registry.groups;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.DirectoryScopes;
import com.google.common.collect.ImmutableSet;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import google.registry.config.ConfigModule.Config;
import google.registry.request.DelegatedOAuthScopes;
import java.util.Set;
import javax.inject.Named;

/**
 * Dagger module for the Google {@link Directory} service.
 *
 * @see google.registry.config.ConfigModule
 * @see google.registry.request.Modules.UrlFetchTransportModule
 * @see google.registry.request.Modules.Jackson2Module
 * @see google.registry.request.Modules.AppIdentityCredentialModule
 * @see google.registry.request.Modules.UseAppIdentityCredentialForGoogleApisModule
 */
@Module
public final class DirectoryModule {

  /** Provides OAuth2 scopes for the Directory service needed by Domain Registry. */
  @Provides
  @ElementsIntoSet
  @DelegatedOAuthScopes
  static Set<String> provideDirectoryOAuthScopes() {
    return ImmutableSet.of(
        DirectoryScopes.ADMIN_DIRECTORY_GROUP_MEMBER,
        DirectoryScopes.ADMIN_DIRECTORY_GROUP);
  }

  @Provides
  static Directory provideDirectory(
      @Named("delegatedAdmin") GoogleCredential credential,
      @Config("projectId") String projectId) {
    return new Directory.Builder(credential.getTransport(), credential.getJsonFactory(), credential)
        .setApplicationName(projectId)
        .build();
  }
}
