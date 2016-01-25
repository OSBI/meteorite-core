package bi.meteorite.core.api.objects

import java.util.Date

/**
  * Created by bugg on 21/12/15.
  */
trait Event {
  def getId: Int

  def setId(id: Int)

  def getUuid: String

  def setUuid(uuid: String)

  def getClassName: String

  def setClassName(className: String)

  def getEventName: String

  def setEventName(eventName: String)

  def getComment: String

  def setComment(comment: String)

  def getStartDate: Date

  def setStartDate(startDate: Date)

  def getEndDate: Date

  def setEndDate(endDate: Date)

  def getDuration: Long

  def setDuration(duration: Long)
}
