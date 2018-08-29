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

package com.deepaksharma.webaddicted.internal;

import android.content.Intent;
import android.os.Bundle;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.deepaksharma.webaddicted.flattener.DefaultFlattener;
import com.deepaksharma.webaddicted.flattener.Flattener;
import com.deepaksharma.webaddicted.formatter.border.BorderFormatter;
import com.deepaksharma.webaddicted.formatter.border.DefaultBorderFormatter;
import com.deepaksharma.webaddicted.formatter.message.json.DefaultJsonFormatter;
import com.deepaksharma.webaddicted.formatter.message.json.JsonFormatter;
import com.deepaksharma.webaddicted.formatter.message.object.BundleFormatter;
import com.deepaksharma.webaddicted.formatter.message.object.IntentFormatter;
import com.deepaksharma.webaddicted.formatter.message.object.ObjectFormatter;
import com.deepaksharma.webaddicted.formatter.message.throwable.DefaultThrowableFormatter;
import com.deepaksharma.webaddicted.formatter.message.throwable.ThrowableFormatter;
import com.deepaksharma.webaddicted.formatter.message.xml.DefaultXmlFormatter;
import com.deepaksharma.webaddicted.formatter.message.xml.XmlFormatter;
import com.deepaksharma.webaddicted.formatter.stacktrace.DefaultStackTraceFormatter;
import com.deepaksharma.webaddicted.formatter.stacktrace.StackTraceFormatter;
import com.deepaksharma.webaddicted.formatter.thread.DefaultThreadFormatter;
import com.deepaksharma.webaddicted.formatter.thread.ThreadFormatter;
import com.deepaksharma.webaddicted.printer.Printer;

/**
 * Factory for providing default implementation.
 */
public class DefaultsFactory {

  private static final Map<Class<?>, ObjectFormatter<?>> BUILTIN_OBJECT_FORMATTERS;

  static {
    Map<Class<?>, ObjectFormatter<?>> objectFormatters = new HashMap<>();
    objectFormatters.put(Bundle.class, new BundleFormatter());
    objectFormatters.put(Intent.class, new IntentFormatter());
    BUILTIN_OBJECT_FORMATTERS = Collections.unmodifiableMap(objectFormatters);
  }

  /**
   * Create the default JSON formatter.
   */
  public static JsonFormatter createJsonFormatter() {
    return new DefaultJsonFormatter();
  }

  /**
   * Create the default XML formatter.
   */
  public static XmlFormatter createXmlFormatter() {
    return new DefaultXmlFormatter();
  }

  /**
   * Create the default throwable formatter.
   */
  public static ThrowableFormatter createThrowableFormatter() {
    return new DefaultThrowableFormatter();
  }

  /**
   * Create the default thread formatter.
   */
  public static ThreadFormatter createThreadFormatter() {
    return new DefaultThreadFormatter();
  }

  /**
   * Create the default stack trace formatter.
   */
  public static StackTraceFormatter createStackTraceFormatter() {
    return new DefaultStackTraceFormatter();
  }

  /**
   * Create the default border formatter.
   */
  public static BorderFormatter createBorderFormatter() {
    return new DefaultBorderFormatter();
  }

  /**
   * Create the default log flattener.
   */
  public static Flattener createFlattener() {
    return new DefaultFlattener();
  }

  /**
   * Create the default printer.
   */
  public static Printer createPrinter() {
    return Platform.get().defaultPrinter();
  }


  /**
   * Get the builtin object formatters.
   *
   * @return the builtin object formatters
   */
  public static Map<Class<?>, ObjectFormatter<?>> builtinObjectFormatters() {
    return BUILTIN_OBJECT_FORMATTERS;
  }
}
