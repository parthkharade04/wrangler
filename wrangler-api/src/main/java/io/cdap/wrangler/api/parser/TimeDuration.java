/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.api.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class TimeDuration implements Token {
    private final String rawValue;
    private final long milliseconds;

    public TimeDuration(String value) {
        this.rawValue = value;
        this.milliseconds = parse(value);
    }

    private long parse(String value) {
        value = value.trim().toLowerCase();
        double num = Double.parseDouble(value.replaceAll("[^0-9.]", ""));
        if (value.endsWith("ms")) return (long) num;
        if (value.endsWith("s")) return (long) (num * 1000);
        return (long) num;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    @Override
    public Object value() {
        return milliseconds;
    }

    @Override
    public TokenType type() {
        return TokenType.NUMERIC;
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(milliseconds);
    }

    @Override
    public String toString() {
        return milliseconds + " ms";
    }
}
