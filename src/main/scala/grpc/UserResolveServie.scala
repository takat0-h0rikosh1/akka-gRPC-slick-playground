package grpc

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
