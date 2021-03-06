/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import io.fabric8.common.util.Strings;

public final class FabricValidations {

    // see https://tools.ietf.org/html/rfc952, we extend it to allow also UNDERSCORE symbol
    private static final Pattern ALLOWED_CONTAINER_NAMES_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[\\.a-zA-Z0-9_-]*$");

    // we allow using dot in profile names
    private static final Pattern ALLOWED_PROFILE_NAMES_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[\\.a-zA-Z0-9_-]*$");
    
    // we allow using dot in service names
    private static final Pattern ALLOWED_VERSION_NAMES_PATTERN = Pattern.compile("^[a-zA-Z0-9]+[\\.a-zA-Z0-9_-]*$");

    private FabricValidations() {
        //Utility Class
    }

    public static void validateProfileNames(Collection<String> profileNames) {
        if (profileNames != null && !profileNames.isEmpty()) {
            for (String profileName : profileNames)
                validateProfileName(profileName);
        }
    }

    public static void validateProfileName(String profileName) {
        if (!isValidProfileName(profileName)) {
            throw new IllegalArgumentException("Profile name '" + profileName + "' is invalid. Profile name must be: letters, numbers, and . _ or - characters");
        }
    }
    
    public static void validateVersionName(String versionName) {
        if (!isValidVersionName(versionName)) {
            throw new IllegalArgumentException("Version name '" + versionName + "' is invalid. Version name must be: letters, numbers, and . _ or - characters");
        }
    }

    public static void validateContainerNames(List<String> containerNames) {
        if (containerNames != null && !containerNames.isEmpty()) {
            for (String containerName : containerNames) {
                validateContainerName(containerName);
            }
        }
    }

    public static void validateContainerName(String containerName) {
        if (!isValidContainerName(containerName)) {
            throw new IllegalArgumentException("Container name '" + containerName + "' is invalid. Container name must be: letters, numbers, and _ or - characters");
        }
    }

    /**
     * @deprecated use {@link #isValidContainerName(String)}
     */
    @Deprecated
    public static boolean isValidName(String containerName) {
        return isValidContainerName(containerName);
    }

    public static boolean isValidContainerName(String containerName) {
       return containerName != null && !containerName.isEmpty() && ALLOWED_CONTAINER_NAMES_PATTERN.matcher(containerName).matches();
    }

    public static boolean isValidProfileName(String name) {
       return name != null && !name.isEmpty() && ALLOWED_PROFILE_NAMES_PATTERN.matcher(name).matches();
    }
    
    public static boolean isValidVersionName(String name) {
        return name != null && !name.isEmpty() && ALLOWED_VERSION_NAMES_PATTERN.matcher(name).matches();
     }

    public static boolean isURIValid(String uriString) {
        URI uri = null;
        try {
            uri = new URI(uriString);
        } catch (URISyntaxException e) {
            return false;
        }

        if (Strings.isEmpty(uri.getHost())) {
            return false;
        }
        String userInfo = uri.getUserInfo();
        String authority = uri.getAuthority();
        String hostPort = authority;
        if (userInfo != null && hostPort.contains("@")) {
            hostPort = hostPort.substring(hostPort.lastIndexOf("@"));
        }
        if (uri.getPort() == -1 && !hostPort.equals(uri.getHost())) {
            return false;
        }

        return true;
    }

}
