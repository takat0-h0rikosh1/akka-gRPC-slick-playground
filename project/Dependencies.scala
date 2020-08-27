import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.0"

  object postgres {
    val postgresDriver = "org.postgresql" % "postgresql" % "42.2.14"
  }

  object slick {
    val slickVersion  = "3.3.2"
    val slick         = "com.typesafe.slick" %% "slick" % slickVersion
    val slickHikaricp = "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
    val slickCodegen  = "com.typesafe.slick" %% "slick-codegen" % slickVersion
  }

  object airframe {
    val airframeVersion = "20.6.2"
    val airframe        = "org.wvlet.airframe" %% "airframe" % airframeVersion
  }

  object akka {
    val akkaHttpVersion     = "10.1.12"
    val akkaVersion         = "2.6.8"
    val akkaHttpCorsVersion = "0.4.2"
    val akkaHttp            = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
    val akkaStream          = "com.typesafe.akka" %% "akka-stream" % akkaVersion
    val akkaDiscovery       = "com.typesafe.akka" %% "akka-discovery" % akkaVersion
    val akkaProtobuf        = "com.typesafe.akka" %% "akka-protobuf" % akkaVersion
  }

  object logback {
    val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"
  }
}
