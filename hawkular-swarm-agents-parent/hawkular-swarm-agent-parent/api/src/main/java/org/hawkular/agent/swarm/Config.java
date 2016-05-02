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

/**
 * @author Bob McWhirter
 */
public class Config {

    private String name;

    private String path;

    private String attribute;

    public Config(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public Config path(String path) {
        this.path = path;
        return this;
    }

    public String path() {
        return this.path;
    }

    public Config attribute(String attribute) {
        this.attribute = attribute;
        return this;
    }

    public String attribute() {
        return this.attribute;
    }
}
