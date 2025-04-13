/*
  * Copyright © 2017-2019 Cask Data, Inc.
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

 package io.cdap.wrangler.directives;

 import io.cdap.wrangler.api.Row;
 import io.cdap.wrangler.api.DirectiveContext;
 import io.cdap.wrangler.api.Arguments;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 
 import java.util.*;
 
 import static org.junit.jupiter.api.Assertions.*;
 
 public class AggregateStatsDirectiveTest {
 
     private AggregateStatsDirective directive;
     private DirectiveContext context;
 
     @BeforeEach
     public void setup() throws Exception {
         directive = new AggregateStatsDirective();
 
         Map<String, String> argsMap = new HashMap<>();
         argsMap.put("sourceSize", "size");
         argsMap.put("sourceTime", "duration");
         argsMap.put("outputSize", "totalSize");
         argsMap.put("outputTime", "totalTime");
 
         Arguments args = key -> argsMap.get(key);
         context = new DirectiveContext("ns", "name", args);
 
         directive.initialize(context);
     }
 
     @Test
     public void testAggregateStats() throws Exception {
         List<Row> inputRows = new ArrayList<>();
         inputRows.add(new Row("size", "1MB", "duration", "3s"));
         inputRows.add(new Row("size", "512KB", "duration", "1500ms"));
 
         List<Row> result = directive.execute(inputRows, context);
 
         assertEquals(1, result.size());
         Row output = result.get(0);
 
         long expectedBytes = 1024 * 1024 + 512 * 1024; // 1MB + 512KB
         long expectedMs = 3000 + 1500;                 // 3s + 1500ms
 
         assertEquals(expectedBytes, output.getValue("totalSize"));
         assertEquals(expectedMs, output.getValue("totalTime"));
     }
 }
 