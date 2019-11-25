import DemoFragment.{Select, Where}
import cats._
import cats.effect._
import cats.data._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.Fragments.whereAndOpt
import shapeless._
import ConnectionIOExecutor._
import Mapper._

object DBConnector {

  type Account = String :: String :: Long :: HNil

  sealed case class Level(code: String) {
    def encode = s"[[ $code ]]"
  }

  def mapTypeToLevel(typeOf: String) = Level(typeOf)

  def main(args: Array[String]): Unit = {
    for {
      dbRes <- query(Some(400), Some("Emperor"))
      mappedRes <-  Option {
        mapType(dbRes) { mapTypeToLevel }
      }
    } yield println(mappedRes)

    for {
     dbRes <- query(Some(400), None)
     mappedRes <- Option {
       mapType(dbRes) { mapTypeToLevel }
     }
    } yield println(mappedRes)

  }


  def query(balanceGreaterThan: Option[Double], typeOfAccount: Option[String]) = {
    val query = queryAccount(db = "bank", balanceGreaterThan, typeOfAccount)

    val compiledStream = query
      .stream
      .compile
      .toList

    executeQuery[List[Account]] { compiledStream }
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