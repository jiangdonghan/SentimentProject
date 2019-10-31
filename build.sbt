name := "SentiProject"
 
version := "1.0" 
      
lazy val `sentiproject` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
libraryDependencies += "JohnSnowLabs" % "spark-nlp" % "2.1.0"
libraryDependencies +="org.apache.spark" %% "spark-core" % "2.3.4"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.3.4"