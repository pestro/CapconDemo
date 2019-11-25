import doobie.implicits._
import doobie.Fragment.const

object DemoFragment {

  object Select {
    def accountSummaries(table: String) = fr"select type, balance from" ++ const { table };
    def accountDetails(table: String) = fr"select id, name, type, balance from" ++ const { table };
  }

  object Where {

    def balance(greaterThan: Option[Double]) =
      greaterThan.map { value => fr"balance > $value" }

    def typeOf(typeOf: Option[String]) =
      typeOf.map { value => fr"type = $value" }
  }
}
