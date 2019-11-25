import shapeless.{::, HList, HNil, Poly1, _}

object Mapper {

  object encoder extends Poly1 {
    import Data._
    import Data.Processed._

    // Can either go from A -> A or...
    implicit def caseRank = at[Rank]{
      case Rank(code) => Rank(s"$code has been encoded!")
    }

    // ...from A -> A'
    implicit def caseId = at[Id] {
      case Id(id) => EncodedId(s"$id has been encoded!")
    }

    implicit def caseName = at[Name] {
      case Name(name) => s"$name has been encoded!"
    }

    implicit def caseBalance = at[Balance] {
      case Balance(balance) => Balance(balance / 10)
    }
  }

}
