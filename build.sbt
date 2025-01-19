import sbt.Keys.libraryDependencies
import scoverage.ScoverageKeys.coverageExcludedFiles

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

lazy val root = (project in file("."))
    .settings(
        name := "SE_Kitty_Card_1",
        coverageOutputCobertura := true,
        coverageEnabled := true,
        coverageOutputXML := true,
        coverageExcludedFiles := "src/main/scala/aview/gui/Gui.scala"
    )

libraryDependencies ++= Seq(
    "com.google.inject" % "guice" % "7.0.0",
    "org.scalactic" %% "scalactic" % "3.2.18",
    "org.scalatest" %% "scalatest" % "3.2.18" % "test",
    "org.scalatestplus" %% "mockito-4-5" % "3.2.12.0" % "test",
    "com.typesafe.play" %% "play-json" % "2.10.5",
    "org.openjfx" % "javafx-media" % "23.0.1",
    "org.scalamock" %% "scalamock" % "6.0.0" % "test",
    "com.typesafe.play" %% "play-json" % "2.10.3",
    "org.scala-lang.modules" %% "scala-xml" % "2.2.0",
    "org.scalafx" %% "scalafx" % "22.0.0-R33"
)