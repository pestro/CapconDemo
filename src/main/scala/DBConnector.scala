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
    import DemoFragment._

    //Fragment.const(obj)

    (Select.accounts ++ Where.balance(400))
      .query[Account]
      .stream
      .compile
      .toList
      .transact(xa)
      .unsafeRunSync
      .foreach(println)
  }
}

class DBConnector(xa: Transactor[IO]) {
  def executeQuery() = {
    xa
  }
}