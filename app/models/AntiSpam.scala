package models

import org.joda.time.DateTime
import play.api.libs.json.{Writes, Reads, Json}
import play.api.libs.ws.WS

import scala.concurrent.Future

case class BadWord(
  id: Long,
  word: String,
  createdAt: DateTime,
  createdBy: Long,
  linkUrlsOnly: Boolean
)

object BadWord {

  implicit val yourJodaDateReads = Reads.jodaDateReads("yyyy-MM-dd'T'HH:mm:ss'Z'")
  implicit val yourJodaDateWrites = Writes.jodaDateWrites("yyyy-MM-dd'T'HH:mm:ss'Z'")
  implicit val badwordFmt = Json.format[BadWord]

  def list: Future[List[BadWord]] = {

    import play.api.Play.current
    import scala.concurrent.ExecutionContext.Implicits.global

    WS.url(s"http://discussion.code.dev-guardianapis.com/discussion-api/moderation/antispam").get map { response =>
      (response.json \ "badwords").as[List[BadWord]]
    }
  }
}
