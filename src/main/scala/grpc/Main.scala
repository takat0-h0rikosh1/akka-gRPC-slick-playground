package grpc

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import wvlet.airframe.{bind, newDesign}

import scala.concurrent.Future

object Main extends App {

  val conf = ConfigFactory
    .parseString("akka.http.server.preview.enable-http2 = on")
    .withFallback(ConfigFactory.defaultApplication())
  val system = ActorSystem("GRPCServer", conf)

  val dbConfig: DatabaseConfig[JdbcProfile] =
    DatabaseConfig.forConfig[JdbcProfile](path = "mydb")

  val design = newDesign
    .bind[JdbcProfile]
    .toInstance(dbConfig.profile)
    .bind[JdbcProfile#Backend#Database]
    .toInstance(dbConfig.db)
    .bind[UserRepository]
    .to[UserRepositoryImpl]
    .bind[ActorSystem]
    .toInstance(system)
    .add(userServiceComponent.design)
    .add(grpcComponent.design)

  design.newSession.build[GRPCServer].run()
}

object userServiceComponent {
  val design = newDesign
    .bind[UserResolveService]
    .toSingleton
}

trait UserResolveService {
  private val repository = bind[UserRepository]
  def getAll: Future[Seq[Tables.UsersRow]] =
    repository.getUser
}

trait UserRepository {
  def getUser: Future[Seq[Tables.UsersRow]]
}

class UserRepositoryImpl(val profile: JdbcProfile, val db: JdbcProfile#Backend#Database) extends UserRepository {
  import profile.api._
  def getUser: Future[Seq[Tables.UsersRow]] = db.run(Tables.Users.result)
}
