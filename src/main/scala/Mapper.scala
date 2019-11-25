import shapeless.{::, HList, HNil, Poly1, _}
import Data._, Data.Id, Data.Processed._

object Mapper {

  object convert {
    trait name extends Poly1 {
      implicit def caseName = at[Name] {
        case Name(name) => s"$name has been converted!"
      }
    }

    trait balance extends Poly1 {
      implicit def caseBalance = at[Balance] {
        case Balance(balance) => Balance(balance / 10)
      }
    }
  }

  object encoder extends Poly1 with convert.balance with convert.name {
    // Can either go from A -> A or...
    implicit def caseRank = at[Rank]{
      case Rank(code) => Rank(s"$code has been encoded!")
    }

    // ...from A -> A'
    implicit def caseId = at[Id] {
      case Id(id) => EncodedId(s"$id has been encoded!")
    }
  }

  // We can now use the granular conversions in order to setup more encompassing strategies
  object convertAccountSummaries extends convert.balance with convert.name
}
