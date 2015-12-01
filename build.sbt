name := """play-angular-jwt"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe" % "config" % "1.3.0",
  "com.adrianhurt" %% "play-bootstrap3" % "0.4.4-P24",
  "com.gu" %% "play-googleauth" % "0.3.1",
  "io.igl" %% "jwt" % "1.2.0",
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
