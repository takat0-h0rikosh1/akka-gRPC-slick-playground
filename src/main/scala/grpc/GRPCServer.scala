package grpc

import akka.actor.ActorSystem
import akka.grpc.scaladsl.{ServerReflection, ServiceHandler}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.settings.ServerSettings
import myapp.proto.user.{UserService, UserServicePowerApiHandler}
import wvlet.airframe.{bind, newDesign, Session}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object GRPCComponent {
  val design = newDesign
    .bind[UserServiceImpl]
    .toSingleton
    .bind[GRPCServer]
    .toSingleton
}

trait GRPCServer {

  private val session               = bind[Session]
  private val userServiceImpl       = bind[UserServiceImpl]
  implicit val system: ActorSystem  = bind[ActorSystem]
  implicit val ec: ExecutionContext = bind[ExecutionContext]

  def run(): Future[Http.ServerBinding] = {

    val service: PartialFunction[HttpRequest, Future[HttpResponse]] =
      UserServicePowerApiHandler.partial(userServiceImpl)

    val reflection: PartialFunction[HttpRequest, Future[HttpResponse]] =
      ServerReflection.partial(List(UserService))

    val bound = Http().bindAndHandleAsync(
      ServiceHandler.concatOrNotFound(service, reflection),
      interface = "127.0.0.1",
      port = 8080,
      settings = ServerSettings(system)
    )

    bound.onComplete {
      case Success(binding) =>
        system.log.info(
          s"gRPC Server online at http://${binding.localAddress.getHostName}:${binding.localAddress.getPort}/"
        )
      case Failure(ex) =>
        system.log.error(ex, "occurred error")
    }

    sys.addShutdownHook {
      bound
        .flatMap(_.unbind())
        .onComplete { _ =>
          system.terminate()
          session.shutdown
        }
    }

    bound
  }
}
