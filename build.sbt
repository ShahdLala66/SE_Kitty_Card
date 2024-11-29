import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "SE_Kitty_Card_1",
    coverageOutputCobertura := true,
    coverageEnabled := true,
    coverageOutputXML := true

  )


libraryDependencies ++= Seq(
  "com.google.inject" % "guice" % "7.0.0",
  "org.scalactic" %% "scalactic" % "3.2.18",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test",
  "org.scalatestplus" %% "mockito-4-5" % "3.2.12.0" % "test",
  "com.typesafe.play" %% "play-json" % "2.10.5"
)