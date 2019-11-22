import cats._
import cats.effect._
import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import shapeless._
import TransactorExecutor._

object DBConnector {

  type Account = String :: Long :: HNil

  def main(args: Array[String]): Unit = {
    firstFive
  }

  def firstFive = {
    sql"select id, balance from bank.accounts"
      .query[Account]    // Query0[String]
      .stream           // Stream[ConnectionIO, String]
      .take(5)          // Stream[ConnectionIO, String]
      .compile.toList   // ConnectionIO[List[String]]
      .transact(xa)     // IO[List[String]]
      .unsafeRunSync    // List[String]
      .foreach(println)
  }
}

class DBConnector(xa: Transactor[IO]) {
  def executeQuery() = {
    xa
  }
}