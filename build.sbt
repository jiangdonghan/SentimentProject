name := "SentiProject"
 
version := "1.0" 
      
lazy val `sentiproject` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven/"
)
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
libraryDependencies += "JohnSnowLabs" % "spark-nlp" % "2.1.0"
val sparkVersion = "2.4.4"
lazy val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion
)
lazy val requiredDependencies = Seq(
  "org.apache.commons" % "commons-lang3" % "3.8"
)

libraryDependencies ++= requiredDependencies ++
  sparkDependencies.map(_ % "compile")
resolvers += Resolver.mavenLocal