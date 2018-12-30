import sbt._
import Keys._

name := "amazone-reviews-analyzer"


version := "0.1"

scalaVersion := "2.11.0"

val sparkVersion = "2.2.0"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => sbtassembly.MergeStrategy.discard
  case x => sbtassembly.MergeStrategy.first
}

libraryDependencies ++= Seq("org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.scalanlp" %% "breeze" % "0.12",
  "org.scalanlp" %% "breeze-viz" % "0.12",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.5.2"
)

resolvers ++= Seq(
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)