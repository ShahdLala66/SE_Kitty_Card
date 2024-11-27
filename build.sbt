import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
  .settings(
    name := "SE_Kitty_Card_1",
    coverageEnabled := true,
    coverageOutputXML := true
  )

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.18",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test",
  "org.scalatestplus" %% "mockito-4-5" % "3.2.12.0" % "test"
)