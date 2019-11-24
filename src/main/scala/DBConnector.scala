import DemoFragment.{Select, Where}
import cats._
import cats.effect._
import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.Fragments.whereAndOpt
import shapeless._
import TransactorExecutor._

object DBConnector {

  type Account = String :: String :: Long :: HNil

  def main(args: Array[String]): Unit = {
    query(Some(400), Some("Emperor"))
    query(Some(400), None)
  }

  def query(balanceGreaterThan: Option[Double], typeOfAccount: Option[String]) = {
    println("----- QUERY START -----")
    queryAccount(db = "bank",
      balanceGreaterThan = balanceGreaterThan,
      typeOfAccount = typeOfAccount
    )
      .stream
      .compile
      .toList
      .transact(xa)
      .unsafeRunSync
      .foreach(println)
    println("----- QUERY END -----")
  }

  def queryAccount(db: String, balanceGreaterThan: Option[Double], typeOfAccount: Option[String]) = {
    (
      Select.accounts(s"$db.accounts") ++ whereAndOpt(
        Where.balance(greaterThan = balanceGreaterThan),
        Where.typeOf(typeOf = typeOfAccount)
      )
    )
    .query[Account]
  }
}