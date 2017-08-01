name := """akkademy-db-client"""

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  // Uncomment to use Akka
  //"com.typesafe.akka" % "akka-actor_2.11" % "2.3.9",
  "com.akkademy-db"   %% "akkademy-db"     % "0.0.1-SNAPSHOT",
  "junit"             % "junit"           % "4.12"  % "test"
)
