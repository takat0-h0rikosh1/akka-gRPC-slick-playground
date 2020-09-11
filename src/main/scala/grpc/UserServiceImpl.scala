package grpc

import akka.grpc.scaladsl.Metadata
import myapp.proto.user.{GetUserListRequest, GetUserListResponse, UserServicePowerApi}
import wvlet.airframe.bind

import scala.concurrent.{ExecutionContext, Future}

trait UserServiceImpl extends UserServicePowerApi {

  private val userResolveService: UserResolveService = bind[UserResolveService]
  implicit val ec: ExecutionContext                  = bind[ExecutionContext]

  override def getUserList(in: GetUserListRequest, metadata: Metadata): Future[GetUserListResponse] = {

    userResolveService.getAll.map(us =>
      GetUserListResponse(
        us.map(u =>
          myapp.proto.user.User(
            u.id,
            u.email,
            u.name,
            myapp.proto.user.User.UserRole.ADMIN
          )
        ) ++
        Seq(
          myapp.proto.user.User(
            1,
            "user1@texample.com",
            "user1",
            myapp.proto.user.User.UserRole.MEMBER
          ),
          myapp.proto.user.User(
            2,
            "user2@texample.com",
            "user2",
            myapp.proto.user.User.UserRole.ADMIN
          ),
          myapp.proto.user.User(
            3,
            "user3@texample.com",
            "user3",
            myapp.proto.user.User.UserRole.MANAGER
          )
        )
      )
    )
  }

}
