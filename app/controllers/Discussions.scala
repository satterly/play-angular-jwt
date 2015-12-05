package controllers

import models.Discussion
import play.api._
import play.api.mvc._
import play.api.libs.json._

class Discussions extends Controller with AuthActions {

  import scala.concurrent.ExecutionContext.Implicits.global

  def list = ???

  def show(key: String) = TokenAuthAction.async { implicit request =>

      Discussion.findByKey(key).map { discussion =>
        Ok(Json.obj(
          "uri" -> s"/discussions/$key",
          "data" -> discussion,
          "total" -> 1
        ))
      }
  }
}
