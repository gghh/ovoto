package ovoto.math.unifi.it.server;

/* Copyright (c) 2008 Google Inc.
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
//package com.google.gdata.util.common.base;


/**
 * Some common string manipulation utilities.
 */
public class Util{

    /**
     * Escape a string for use inside as XML single-quoted attributes. This
     * escapes less-than, single-quote, ampersand, and (not strictly necessary)
     * newlines.
     */
    public static String xmlSingleQuotedEscape(String s) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
          case '\'': sb.append("&apos;"); break;
          case '&': sb.append("&amp;"); break;
          case '<': sb.append("&lt;"); break;
          case '\n': sb.append("&#xA;"); break;

          case '\000': case '\001': case '\002': case '\003': case '\004':
          case '\005': case '\006': case '\007': case '\010': case '\013':
          case '\014': case '\016': case '\017': case '\020': case '\021':
          case '\022': case '\023': case '\024': case '\025': case '\026':
          case '\027': case '\030': case '\031': case '\032': case '\033':
          case '\034': case '\035': case '\036': case '\037':
            // do nothing, these are disallowed characters
            break;
          default:   sb.append(c);
        }
      }
      return sb.toString();
    }
}
