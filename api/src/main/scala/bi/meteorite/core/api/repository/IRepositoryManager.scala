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
package bi.meteorite.core.api.repository

import bi.meteorite.core.api.objects.MeteoriteNode
import bi.meteorite.core.api.objects.MeteoriteUser

/**
  * Repository operations allowed from within Meteorite Core.
  */
trait IRepositoryManager {
  /**
    * Initialize the repository.
    */
  def init

  /**
    * Start the repository.
    */
  def start

  /**
    * Stop the repository.
    */
  def stop

  /**
    * Add an object to the repository.
    *
    * @param node the file/folder object.
    */
  def addNode(node: MeteoriteNode)

  /**
    * Remove an object from the repository.
    *
    * @param node the file/folder object
    */
  def removeNode(node: MeteoriteNode)

  /**
    * Move an object within the repository.
    *
    * @param from from location.
    * @param to   to location.
    * @param user the user performing the operation.
    */
  def moveNode(from: String, to: String, user: MeteoriteUser)

  /**
    * Copy an object within the repository.
    *
    * @param from from location.
    * @param to   to location.
    * @param user the user performing the operation.
    */
  def copyNode(from: String, to: String, user: MeteoriteUser)

  /**
    * Remove the repository from the server.
    */
  def dropRepository

  /**
    * Export the repository to a file.
    *
    * @return the repository.
    */
  def exportRepository: Array[Byte]

  /**
    * Restore a backup.
    *
    * @param data the file.
    */
  def restoreResponsitory(data: Array[Byte])

  /**
    * Rollback an object to a previous version.
    *
    * @param node the node to rollback on.
    */
  def rollbackNode(node: MeteoriteNode)
}

