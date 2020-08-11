package grpc

import slick.jdbc.JdbcProfile
import wvlet.airframe.{bind, newDesign}

import scala.concurrent.Future

object UserServiceComponent {
  val design = newDesign
    .bind[UserResolveService]
    .toSingleton
    .bind[UserRepository]
    .to[UserRepositoryImpl]
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
