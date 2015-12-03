package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

class Application extends Controller with AuthActions {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def json = TokenAuthAction { implicit request =>
    Ok(Json.obj(
      "data" -> "foo",
      "total" -> 4
    ))
  }

}
