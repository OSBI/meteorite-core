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
 * Repository operations allowed from within Meteorite Core.
 */
public interface IRepositoryManager {

  /**
   * Initialize the repository.
   */
  void init();

  /**
   * Start the repository.
   */
  void start();

  /**
   * Stop the repository.
   */
  void stop();

  /**
   * Add an object to the repository.
   * @param node the file/folder object.
   */
  void addNode(MeteoriteNode node);

  /**
   * Remove an object from the repository.
   * @param node the file/folder object
   */
  void removeNode(MeteoriteNode node);

  /**
   * Move an object within the repository.
   * @param from from location.
   * @param to to location.
   * @param user the user performing the operation.
   */
  void moveNode(String from, String to, MeteoriteUser user);

  /**
   * Copy an object within the repository.
   * @param from from location.
   * @param to to location.
   * @param user the user performing the operation.
   */
  void copyNode(String from, String to, MeteoriteUser user);

  /**
   * Remove the repository from the server.
   */
  void dropRepository();

  /**
   * Export the repository to a file.
   * @return the repository.
   */
  byte[] exportRepository();

  /**
   * Restore a backup.
   * @param data the file.
   */
  void restoreResponsitory(byte[] data);

  /**
   * Rollback an object to a previous version.
   * @param node the node to rollback on.
   */
  void rollbackNode(MeteoriteNode node);

}

