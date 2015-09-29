/*
 * Copyright 2015 OSBI Ltd
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

package bi.meteorite.core.api.security.exceptions;

/**
 * Token Provider Exception
 */
public class TokenProviderException extends Exception {
  // The serial version UID of this exception class
  private static final long serialVersionUID = 1008471221110336338L;

  /**
   * Constructs a new token provider exception.
   *
   * @param msg An error message providing more information about the cause of the error.
   */
  public TokenProviderException(final String msg) {
    super(msg);
  }

  /**
   * Constructs a new token provider exception.
   *
   * @param t An exception providing more information about the cause of the error.
   */
  public TokenProviderException(final Throwable t) {
    super(t);
  }
}
