import doobie.implicits._

object DemoFragment {

  object Select {
    def accounts = fr"select id, balance from bank.accounts";
  }

  object Where {
    def balance(greaterThan: Double) = fr"where balance > $greaterThan"
  }
}
