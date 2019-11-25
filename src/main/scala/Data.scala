object Data {
  sealed case class Id(id: String)
  sealed case class Name(name: String)
  sealed case class Rank(rank: String)
  sealed case class Balance(balance: Double)

  object Processed {
    sealed case class EncodedId(id: String)
  }
}
