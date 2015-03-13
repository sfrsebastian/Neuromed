name := """Arquisoft"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
	javaJdbc,
	javaJpa.exclude("org.hibernate.javax.persistence", "hibernate-jpa-2.0-api"),
	"org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final",
	"postgresql" % "postgresql" % "9.1-901-1.jdbc4"
)
