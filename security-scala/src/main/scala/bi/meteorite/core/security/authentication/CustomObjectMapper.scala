/*
 * Copyright 2016 OSBI Ltd
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

package bi.meteorite.core.security.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
  * Extend the default Jackson Object Mapper.
  */
class CustomObjectMapper extends ObjectMapper{
  this.registerModule(DefaultScalaModule)
  this.registerModule(new Hibernate4Module())
}
