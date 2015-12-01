package controllers

import io.igl.jwt._
import play.api.libs.ws.WSAPI
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import com.typesafe.config.ConfigFactory
import play.api.libs.json.Json
import scala.concurrent.Future
import com.gu.googleauth._
import play.api.Play.current
import org.joda.time.Duration

trait AuthActions extends Actions {
  val loginTarget: Call = routes.Login.loginAction()
  val authConfig = OAuth.googleAuthConfig

  def tokenChecker(request: RequestHeader) = {

    val claims = request.headers.get("Authorization") match {
      case Some(jwt) => DecodedJwt.validateEncodedJwt(jwt, "secret", Algorithm.HS256, Set(Typ), Set(Iss))
      case _ => None
    }
    println(claims.toString)


    Some(UserIdentity(email = "foo", sub = "foo",
      firstName = "foo", lastName = "foo", exp = 1L, avatarUrl = Some("foo")))
    None
  }


object MyAuthAction extends AuthenticatedBuilder(r => tokenChecker(r), r => sendForAuth(r))
}

object OAuth {
  val config = ConfigFactory.load()
  val oauth2clientId = config.getString("google.clientId")
  val oauth2secret = config.getString("google.clientSecret")
  val oauth2callback = config.getString("google.redirectUrl")

  val ANTI_FORGERY_KEY = "antiForgeryToken"
  val googleAuthConfig =
    GoogleAuthConfig(
      oauth2clientId,
      oauth2secret,
      oauth2callback,
      Some("guardian.co.uk"), // Google App domain to restrict login
      Some(Duration.standardHours(24)), // hours before forcing the user to re-enter their credentials
      true // Re-authenticate (without prompting) with google when session expires
    )
}

class Login extends Controller with AuthActions {


  def login = Action { implicit request =>
    println(request.session.toString)
    val error = request.flash.get("error")
    Ok
  }

  /*
  Redirect to Google with anti forgery token (that we keep in session storage - note that flashing is NOT secure)
   */
  def loginAction = Action.async { implicit request =>
    println(request.session.toString)
    val antiForgeryToken = GoogleAuth.generateAntiForgeryToken()
    GoogleAuth.redirectToGoogle(OAuth.googleAuthConfig, antiForgeryToken).map {
      _.withSession { request.session + (OAuth.ANTI_FORGERY_KEY -> antiForgeryToken) }
    }
  }

  /*
  User comes back from Google.
  We must ensure we have the anti forgery token from the loginAction call and pass this into a verification call which
  will return a Future[UserIdentity] if the authentication is successful. If unsuccessful then the Future will fail.

   */
  def oauth2Callback = Action.async { implicit request =>
    val session = request.session
    session.get(OAuth.ANTI_FORGERY_KEY) match {
      case None =>
        Future.successful(Redirect(routes.Login.login()).flashing("error" -> "Anti forgery token missing in session"))
      case Some(token) =>
        GoogleAuth.validatedUserIdentity(OAuth.googleAuthConfig, token).map { identity =>
          // We store the URL a user was trying to get to in the LOGIN_ORIGIN_KEY in AuthAction
          // Redirect a user back there now if it exists

          val jwt = new DecodedJwt(Seq(Alg(Algorithm.HS256), Typ("JWT")), Seq(Iss("readme")))
          val fragment = "#token_type=Bearer&id_token=" + jwt.encodedAndSigned("secret")

          val redirect = session.get(LOGIN_ORIGIN_KEY) match {
            case Some(url) => Redirect(url + fragment)
            case None => Redirect(routes.Application.index() + fragment)
          }
          // Store the JSON representation of the identity in the session - this is checked by AuthAction later
          redirect.withSession {
            session - OAuth.ANTI_FORGERY_KEY - LOGIN_ORIGIN_KEY
          }
        } recover {
          case t =>
            // you might want to record login failures here - we just redirect to the login page
            Redirect(routes.Login.login())
              .withSession(session - OAuth.ANTI_FORGERY_KEY)
              .flashing("error" -> s"Login failure: ${t.toString}")
        }
    }
  }

  def logout = Action { implicit request =>
    Redirect(routes.Application.index()).withNewSession
  }

  def userInfo = Action { implicit request =>
    Ok("foo")
  }
}
