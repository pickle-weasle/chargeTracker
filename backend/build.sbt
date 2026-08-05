name := """backend"""
organization := "com.johnwynne"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
libraryDependencies += jdbc
libraryDependencies += "com.h2database" % "h2" % "2.4.240"
libraryDependencies += evolutions

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.johnwynne.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.johnwynne.binders._"
