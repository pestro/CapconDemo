import cats.effect.{Blocker, IO}
import doobie.Transactor
import doobie.util.ExecutionContexts

object TransactorExecutor {
  // We need a ContextShift[IO] before we can construct a Transactor[IO]
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  // A transactor that gets connections from java.sql.DriverManager and executes blocking operations on a synchronous EC
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",                    // driver classname
    "jdbc:postgresql://localhost:26257/bank",     // connect URL (driver-specific)
    "maxroach",                                  // user
    null,                                       // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous).blockingContext // Blocking Context
  )
}
