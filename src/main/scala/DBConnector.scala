import cats._
import cats.effect._
import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import shapeless._

object DBConnector {
  // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
  // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  // A transactor that gets connections from java.sql.DriverManager and executes blocking operations
  // on an our synchronous EC. See the chapter on connection handling for more info.
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",     // driver classname
    "jdbc:postgresql://localhost:26257/bank",     // connect URL (driver-specific)
//    "jdbc:postgresql:bank",     // connect URL (driver-specific)
    "maxroach",                  // user
    null,                          // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous).blockingContext // just for testing
  )
  def main(args: Array[String]): Unit = {
    val program1 = 42.pure[ConnectionIO]

    sql"select id, balance from bank.accounts"
      .query[String :: Long :: HNil]    // Query0[String]
      .stream           // Stream[ConnectionIO, String]
      .take(5)          // Stream[ConnectionIO, String]
      .compile.toList   // ConnectionIO[List[String]]
      .transact(xa)     // IO[List[String]]
      .unsafeRunSync    // List[String]
      .foreach(println) // Unit
  }
}
