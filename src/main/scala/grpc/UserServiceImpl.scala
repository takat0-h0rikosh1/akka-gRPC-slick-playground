package grpc

import akka.actor.ActorSystem
import myapp.proto.user.{GetUserListRequest, GetUserListResponse, UserService}
import wvlet.airframe.bind

import scala.concurrent.{ExecutionContextExecutor, Future}

trait UserServiceImpl extends UserService {

  private val userResolveService            = bind[UserResolveService]
  private val system: ActorSystem           = bind[ActorSystem]
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  override def getAll(in: GetUserListRequest): Future[GetUserListResponse] = {

    userResolveService.getAll.map(us =>
      GetUserListResponse(
        us.map(u =>
          myapp.proto.user.User(
            1,
            "t_horikoshi@example.com",
            "t_horikoshi",
            myapp.proto.user.User.UserRole.Admin
          )
        )
      )
    )
  }

}
