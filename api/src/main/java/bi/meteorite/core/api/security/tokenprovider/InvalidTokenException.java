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

package bi.meteorite.core.api.security.tokenprovider;

/**
 * Created by bugg on 20/07/15.
 */
public class InvalidTokenException extends Exception {
  // The serial version UID of this exception class
  private static final long serialVersionUID = 783720889456143935L;

  /**
   * Constructs a new token provider exception.
   *
   * @param msg
   *        An error message providing more information about the cause of the error.
   */
  public InvalidTokenException(final String msg) {
    super(msg);
  }
}
