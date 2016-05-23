package longevity.subdomain.ptype

import emblem.TypeKeyMap
import longevity.exceptions.subdomain.DuplicateEntityTypesException
import longevity.subdomain.entity.EntityTypePool
import longevity.subdomain.persistent.Persistent

/** houses methods for constructing persistent type pools */
object PTypePool {

  /** collects a sequence of [[PType persistent types]] into a [[PTypePool]].
   * @param pTypes the sequence of persistent types stored in the pool
   */
  @throws[DuplicateEntityTypesException]("when two PTypes have the same Persistent type")
  def apply(pTypes: PType[_ <: Persistent]*): PTypePool = {

    val map: TypeKeyMap[Persistent, PType] =
      pTypes.foldLeft(TypeKeyMap[Persistent, PType]()) {
        case (map, pType) => map + (pType.pTypeKey -> pType)
      }

    if (pTypes.size != map.size) throw new DuplicateEntityTypesException

    map
  }

  /** an empty persistent type pool */
  val empty: PTypePool = apply()

}