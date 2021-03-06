package grpc

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import wvlet.airframe.newDesign

import scala.concurrent.ExecutionContext

object Main extends App {

  val conf                 = ConfigFactory.load()
  val system               = ActorSystem("GRPCServer", conf)
  val ec: ExecutionContext = system.dispatcher
  val dbConfig: DatabaseConfig[JdbcProfile] =
    DatabaseConfig.forConfig[JdbcProfile](path = "mydb")

  val design = newDesign
    .bind[JdbcProfile]
    .toInstance(dbConfig.profile)
    .bind[JdbcProfile#Backend#Database]
    .toInstance(dbConfig.db)
    .bind[ActorSystem]
    .toInstance(system)
    .bind[ExecutionContext]
    .toInstance(ec)
    .add(GRPCComponent.design)
    .add(UserServiceComponent.design)

  design.newSession.build[GRPCServer].run()
}
