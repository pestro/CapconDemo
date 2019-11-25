import TransactorExecutor.xa
import doobie.free.connection.ConnectionIO
import doobie.syntax.connectionio._

object ConnectionIOExecutor {

  def executeQuery[T](io: ConnectionIO[T]): T = {
    io.transact(xa).unsafeRunSync
  }
}
