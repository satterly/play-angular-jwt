package models

import com.typesafe.config.ConfigFactory
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws._

import scala.concurrent.Future

case class Discussion(
  key: String,
  title: String,
  // status: String,
  // premoderated: Boolean,
  // watched: Boolean,
  // createdOn: DateTime,
  // closedAfter: DateTime,
  commentCount: Long
)

object Discussion {

  val config = ConfigFactory.load()

  implicit val discussionFmt = Json.format[Discussion]

  def findByKey(key: String): Future[Discussion] = {

    import play.api.Play.current
    import scala.concurrent.ExecutionContext.Implicits.global

    //val discussionApi = config.getString("discussionApi.url").orElse("")

    WS.url(s"http://discussion.guardianapis.com/discussion-api/discussion/$key").get().map { response =>
      (response.json \ "discussion").as[Discussion]
    }
  }
}
