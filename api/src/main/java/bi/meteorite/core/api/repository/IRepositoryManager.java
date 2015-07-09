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

package bi.meteorite.core.api.repository;

import bi.meteorite.core.api.objects.MeteoriteNode;
import bi.meteorite.core.api.objects.MeteoriteUser;

/**
 * Created by bugg on 23/06/15.
 */
public interface IRepositoryManager {

  void init();

  void start();

  void stop();

  void addNode(MeteoriteNode node);

  void removeNode(MeteoriteNode node);

  void moveNode(String from, String to, MeteoriteUser user);

  void copyNode(String from, String to, MeteoriteUser user);

  void dropRepository();

  byte[] exportRepository();

  void restoreResponsitory(byte[] data);

  void rollbackNode(MeteoriteNode node);

}

