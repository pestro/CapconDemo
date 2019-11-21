import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._
import shapeless._


object HikariCP extends IOApp {

  // Resource yielding a transactor configured with a bounded connect EC and an unbounded
  // transaction EC. Everything will be closed and shut down cleanly after use.
  val transactor: Resource[IO, HikariTransactor[IO]] =
  for {
    ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
    be <- Blocker[IO] // our blocking EC
    xa <- HikariTransactor.newHikariTransactor[IO](
      "org.postgresql.Driver", // driver classname
      "jdbc:postgresql://localhost:26257/bank", // connect URL
      "maxroach", // username
      null, // password
      ce, // await connection here
      be.blockingContext // execute JDBC operations here
    )
  } yield xa

  var dbConnector: DBConnector = null

  transactor.use { xa =>
    dbConnector = new DBConnector(xa)
    ExitCode.Success.pure[IO]
  }

  def run(args: List[String]) = IO(ExitCode.Success)

  /*
    transactor.use { xa =>
    // Construct and run your server here!
    for {
      n <- DBConnector.firstFive
      _ <- IO { println(n) }
    } yield ExitCode.Success
   */
}