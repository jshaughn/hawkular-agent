/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
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
package org.hawkular.agent.swarm;

import java.util.ArrayList;
import java.util.List;

import org.wildfly.swarm.spi.api.Fraction;

/**
 * @author Bob McWhirter
 */
public class AgentFraction implements Fraction {

    private String name = "wildfly-swarm";

    private String username;

    private String password;

    private String host = "localhost";

    private int port = 8080;

    private List<ResourceTypeSet> resourceTypeSets = new ArrayList<>();

    public AgentFraction() {

    }

    public static AgentFraction createDefaultHawkularFraction() {
        return new AgentFraction()
                .resourceTypeSet(ResourceTypeSets.MAIN)
                .resourceTypeSet(ResourceTypeSets.DEPLOYMENT);
    }

    public static AgentFraction createDefaultHawkularFraction(String host, int port, String username,
            String password) {
        return createDefaultHawkularFraction()
                .host(host)
                .port(port)
                .username(username)
                .password(password);
    }

    public AgentFraction name(String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return this.name;
    }

    public AgentFraction username(String username) {
        this.username = username;
        return this;
    }

    public String username() {
        return this.username;
    }

    public AgentFraction password(String password) {
        this.password = password;
        return this;
    }

    public String password() {
        return this.password;
    }

    public AgentFraction host(String host) {
        this.host = host;
        return this;
    }

    public String host() {
        return this.host;
    }

    public AgentFraction port(int port) {
        this.port = port;
        return this;
    }

    public int port() {
        return this.port;
    }

    public AgentFraction resourceTypeSet(ResourceTypeSet resourceTypeSet) {
        this.resourceTypeSets.add(resourceTypeSet);
        return this;
    }

    public List<ResourceTypeSet> resourceTypeSets() {
        return this.resourceTypeSets;
    }
}
