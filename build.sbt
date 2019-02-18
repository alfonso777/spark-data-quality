name := "dl-scala"
version := "1.0.1"
scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Artima Maven Repository" at "http://repo.artima.com/releases",
  "Hadoop Releases" at "https://repository.cloudera.com/content/repositories/releases/"
)

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.2" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.2" % "provided"
//libraryDependencies += "junit" % "junit" % "4.12" % "test" % "provided"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @_*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

assemblyJarName in assembly := s"dataquality-${version.value}.jar"
test in assembly := {}
//mainClass in assembly := Some("com.cloudera.sa.examples.tablestats.ReportStatsApp")
