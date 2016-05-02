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

import java.util.concurrent.TimeUnit;

/**
 * @author Bob McWhirter
 */
public class Sampler<T extends Sampler> {

    private final Class<T> cls;

    private String name;

    private String path;

    private String attribute;

    private long interval;

    private TimeUnit timeUnit;

    protected Sampler(Class<T> cls, String name) {
        this.cls = cls;
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public T every(long interval, TimeUnit timeUnit) {
        this.interval = interval;
        this.timeUnit = timeUnit;
        return cls.cast(this);
    }

    public long interval() {
        return this.interval;
    }

    public TimeUnit timeUnit() {
        return this.timeUnit;
    }

    public T path(String path) {
        this.path = path;
        return this.cls.cast(this);
    }

    public String path() {
        return this.path;
    }

    public T attribtue(String attribute) {
        this.attribute = attribute;
        return this.cls.cast(this);
    }

    public String attribute() {
        return this.attribute;
    }


}
