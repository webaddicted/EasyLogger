/*
 * Copyright 2016 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deepaksharma.webaddicted.printer;

import com.deepaksharma.webaddicted.flattener.Flattener;
import com.deepaksharma.webaddicted.internal.DefaultsFactory;

/**
 * Log {@link Printer} using {@code System.out.println(String)}.
 *

 */
public class ConsolePrinter implements Printer {

  /**
   * The log flattener when print a log.
   */
  private Flattener flattener;

  /**
   * Constructor.
   */
  public ConsolePrinter() {
    this.flattener = DefaultsFactory.createFlattener();
  }


  @Override
  public void println(int logLevel, String tag, String msg) {
    String flattenedLog = flattener.flatten(logLevel, tag, msg).toString();
    System.out.println(flattenedLog);
  }
}
