import cats._
import cats.effect._
import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import shapeless._

object DBConnector {
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

  type Account = String :: Long :: HNil

  def main(args: Array[String]): Unit = {
    val transactResult: IO[List[Account]] = for {
      queryStream <- IO { firstFiveSQLStream }
      query <- IO { produceTransactIO[Account](queryStream) }
      transact <- IO { executeTransaction[Account](query) }
      _ <- IO { transact.foreach(println) }
    } yield transact

    transactResult.unsafeRunSync
  }

  def firstFive = {
    sql"select id, balance from bank.accounts"
      .query[String :: Long :: HNil]    // Query0[String]
      .stream           // Stream[ConnectionIO, String]
      .take(5)          // Stream[ConnectionIO, String]
      .compile.toList   // ConnectionIO[List[String]]
      .transact(xa)     // IO[List[String]]
      .unsafeRunSync    // List[String]
  }

  def firstFiveSQLStream = {
    sql"select id, balance from bank.accounts"
      .query[String :: Long :: HNil]    // Query0[String]
      .stream           // Stream[ConnectionIO, String]
      .take(5)          // Stream[ConnectionIO, String]
      .compile.toList   // ConnectionIO[List[String]]
  }

  def produceTransactIO[T](io: ConnectionIO[List[T]]): IO[List[T]] = {
    io.transact(xa)
  }

  def executeTransaction[T](transaction: IO[List[T]]): List[T] = {
    transaction.unsafeRunSync
  }
}

class DBConnector(xa: Transactor[IO]) {
  def executeQuery() = {
    xa
  }
}