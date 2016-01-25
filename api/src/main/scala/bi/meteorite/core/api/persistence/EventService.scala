package bi.meteorite.core.api.persistence

import bi.meteorite.core.api.objects.Event

/**
  * Created by bugg on 21/12/15.
  */
trait EventService {
  def getEventById(id: String): Event

  def getEventByUUID(uuid: String): Event

  def getEventByEventName(name: String): Event

  def addEvent(user: Event): Event

  def getEvents: List[Event]

  def updateEvent(event: Event)

  def deleteEventById(id: String)

  def deleteEventByUUID(uuid: String)
}

