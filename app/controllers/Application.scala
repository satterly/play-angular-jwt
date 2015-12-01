package controllers

import play.api._
import play.api.mvc._

class Application extends Controller with AuthActions {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def authenticated = MyAuthAction { implicit request =>
    Ok(views.html.index())
  }

}
