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
  import Data._

  type AccountSummary = Rank :: Balance :: HNil
  type AccountDetails = Id :: Name :: Rank :: Balance :: HNil

  def main(args: Array[String]): Unit = {
    for {
      dbRes <- querySummary(Some(400), Some("Emperor"))
      mappedRes <-  Option {
        dbRes map { encoder }
      }
    } yield println(mappedRes)

    for {
     dbRes <- queryDetails(Some(400), None)
     mappedRes <- Option {
       dbRes map { encoder }
     }
    } yield println(mappedRes)

  }


  def querySummary(balanceGreaterThan: Option[Double], typeOfAccount: Option[String]) = {
    val query = fragment(Select.accountSummaries("bank.accounts"), balanceGreaterThan, typeOfAccount)
      .query[AccountSummary]
    val compiledStream = query.stream.compile.toList
    executeQuery[List[AccountSummary]] { compiledStream }
  }

  def queryDetails(balanceGreaterThan: Option[Double], typeOfAccount: Option[String]) = {
    val query = fragment(Select.accountDetails("bank.accounts"), balanceGreaterThan, typeOfAccount)
      .query[AccountDetails]
    val compiledStream = query.stream.compile.toList
    executeQuery[List[AccountDetails]] { compiledStream }
  }

  def fragment(selectFragment: Fragment, balanceGreaterThan: Option[Double], typeOfAccount: Option[String]) = {
    (
      selectFragment ++ whereAndOpt(
        Where.balance(greaterThan = balanceGreaterThan),
        Where.typeOf(typeOf = typeOfAccount)
      )
    )
  }
}