package grpc

import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class User(
    id: Int,
    email: String,
    name: String,
    role: String
)

trait UserRepository {
  def getUser: Future[Seq[Tables.UsersRow]]
  def store: Future[Unit]
}

class UserRepositoryImpl(val profile: JdbcProfile, val db: JdbcProfile#Backend#Database) extends UserRepository {
  import profile.api._
  def getUser: Future[Seq[Tables.UsersRow]] = db.run(Tables.Users.result)
  def store(): Future[Unit]                 = ???
}
