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

package com.deepaksharma.webaddicted.interceptor;

import java.util.Arrays;

import com.deepaksharma.webaddicted.LogItem;

/**
 * Filter out the logs with a tag that is in the blacklist.
 *

 */
public class BlacklistTagsFilterInterceptor extends AbstractFilterInterceptor {

  private Iterable<String> blacklistTags;

  /**
   * Constructor
   *
   * @param blacklistTags the blacklist tags, the logs with a tag that is in the blacklist will be
   *                      filtered out
   */
  public BlacklistTagsFilterInterceptor(String... blacklistTags) {
    this(Arrays.asList(blacklistTags));
  }

  /**
   * Constructor
   *
   * @param blacklistTags the blacklist tags, the logs with a tag that is in the blacklist will be
   *                      filtered out
   */
  public BlacklistTagsFilterInterceptor(Iterable<String> blacklistTags) {
    if (blacklistTags == null) {
      throw new NullPointerException();
    }
    this.blacklistTags = blacklistTags;
  }

  /**
   * {@inheritDoc}
   *
   * @return true if the tag of the log is in the blacklist, false otherwise
   */
  @Override
  protected boolean reject(LogItem log) {
    if (blacklistTags != null) {
      for (String disabledTag : blacklistTags) {
        if (log.tag.equals(disabledTag)) {
          return true;
        }
      }
    }
    return false;
  }
}
