/*
 * Copyright 2015 Elvis Hew
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

package com.deepaksharma.webaddicted.formatter.message.throwable;


import com.deepaksharma.webaddicted.internal.util.StackTraceUtil;

/**
 * Simply put each stack trace(method name, source file and line number) of the throwable
 * in a single line.
 */
public class DefaultThrowableFormatter implements ThrowableFormatter {

  @Override
  public String format(Throwable tr) {
    return StackTraceUtil.getStackTraceString(tr);
  }
}
