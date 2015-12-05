package controllers

import models.BadWord
import play.api._
import play.api.mvc._
import play.api.libs.json._

class AntiSpam extends Controller with AuthActions {

  import scala.concurrent.ExecutionContext.Implicits.global

  def list = TokenAuthAction.async { implicit request =>

    BadWord.list.map { badwords =>
      Ok(Json.obj(
        "uri" -> "/api/antispam",
        "data" -> Json.toJson(badwords),
        "total" -> 400
      ))
    }
  }

}
