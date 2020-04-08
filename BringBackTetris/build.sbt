name := "BringBackTetris"
version := "1.8"
//offline := true
//resolvers += "Local Maven Repository" at "file:///"+Path.userHome+ "/.ivy2/cache"
//unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "8.0.181-R13",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.4"
)
//mainClass in assembly := Some("hep88.Boom")
//EclipseKeys.executionEnvironment := Some(EclipseExecutionEnvironment.JavaSE18)
//open program in different process
fork := true 