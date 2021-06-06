name := "algolia-ingestion"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.3.1"

mainClass := Some("com.aroch.spark.Main")

libraryDependencies ++= Seq(

  // spark
  "org.apache.spark" %% "spark-core" % sparkVersion % "compile",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "compile",

  // params
  "org.rogach" %% "scallop" % "3.1.0",

  // testing
  "org.scalatest" %% "scalatest" % "3.0.3" % "test"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _ *) => MergeStrategy.discard
  case x                              => MergeStrategy.first
}