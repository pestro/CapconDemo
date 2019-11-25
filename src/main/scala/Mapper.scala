import DBConnector.Level
import shapeless.{HList, HNil}
import shapeless._

object Mapper {

  def mapType[L <: HList, R](results: L)
                            (typeMapper: String => R) = {
    results match {
      case (id: String) :: (typeOf: String) :: (balance: Long) :: HNil =>
        id :: typeMapper(typeOf) :: balance :: HNil
    }
  }
}
