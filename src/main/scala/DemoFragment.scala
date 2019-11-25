import doobie.implicits._
import doobie.Fragment.const

object DemoFragment {

  object Select {
    def accounts(table: String) = fr"select id, type, balance from" ++ const { table };
  }

  object Where {

    def balance(greaterThan: Option[Double]) =
      greaterThan.map { value => fr"balance > $value" }

    def typeOf(typeOf: Option[String]) =
      typeOf.map { value => fr"type = $value" }
  }
}
