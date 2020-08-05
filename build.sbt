import Dependencies._

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

// Uncomment the following for publishing to Sonatype.
lazy val root = (project in file("."))
  .settings(
    name := "slick-playground",
    slickGen := {
      val dir       = baseDirectory.value
      val r         = (runner in Compile).value
      val cp        = (dependencyClasspath in Compile).value
      val outputDir = (dir / "src/main/scala").getPath
      val result = r.run(
        "slick.codegen.SourceCodeGenerator",
        cp.files,
        Array("slick.jdbc.PostgresProfile", dbDriver, dbUrl, outputDir, "grpc", dbUser, dbPassword),
        streams.value.log
      )
      result.failed.foreach(throw _)
      val fname = outputDir + "Tables.scala"
      Seq(file(fname))
    },
    libraryDependencies ++= Seq(
        postgres.postgresDriver,
        slick.slick,
        slick.slickHikaricp,
        slick.slickCodegen,
        airframe.airframe,
        akka.akkaHttp,
        akka.akkaActorTyped,
        akka.akkaDiscovery,
        akka.akkaStream,
        akka.akkaProtobuf,
        logback.logbackClassic,
        scalaTest % Test
      ),
    akkaGrpcCodeGeneratorSettings += "server_power_apis",
    inConfig(Compile)(
      Seq(
        PB.protoSources += resourceDirectory.value / "protos"
      )
    )
  )
  .enablePlugins(AkkaGrpcPlugin)

val dbDriver    = "org.postgresql.Driver"
val dbName      = "myapp"
val dbUser      = "myapp"
val dbPassword  = "myapp"
val dbPort: Int = 5432
val dbUrl       = s"jdbc:postgresql://localhost:$dbPort/$dbName"

lazy val flyway = (project in file("tools/flyway"))
  .settings(
    libraryDependencies ++= Seq(
        postgres.postgresDriver
      ),
    parallelExecution in Test := false,
    flywayDriver := dbDriver,
    flywayUrl := dbUrl,
    flywayUser := dbUser,
    flywayPassword := dbPassword,
    flywayLocations := Seq(
        s"filesystem:${baseDirectory.value}/src/main/resources/migration/"
      )
  )
  .enablePlugins(FlywayPlugin)

lazy val slickGen = TaskKey[Seq[File]]("slickGen")
