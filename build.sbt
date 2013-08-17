organization := "com.pongr"

name := "diamondhead"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "Spray" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "io.spray" % "spray-json_2.9.2" % "1.2.3",
  "commons-codec" % "commons-codec" % "1.8",
  "org.specs2" %% "specs2" % "1.12.4" % "test"
)

seq(sbtrelease.Release.releaseSettings: _*)

//http://www.scala-sbt.org/using_sonatype.html
//https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots/")
  else                             Some("releases" at nexus + "service/local/staging/deploy/maven2/")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

homepage := Some(url("http://github.com/pongr/diamondhead"))

organizationName := "Pongr"

organizationHomepage := Some(url("http://pongr.com"))

description := "Scala signed requests"

pomExtra := (
  <scm>
    <url>git@github.com:pongr/diamondhead.git</url>
    <connection>scm:git:git@github.com:pongr/diamondhead.git</connection>
  </scm>
  <developers>
    <developer>
      <id>zcox</id>
      <name>Zach Cox</name>
      <url>http://theza.ch</url>
    </developer>
  </developers>
)
