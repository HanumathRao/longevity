package longevity.subdomain

import emblem.TypeKeyMap
import longevity.exceptions.subdomain.DuplicateETypesException

/** contains a factory methods for creating an [[ETypePool embeddable type
 * pool]]
 */
object ETypePool {

  /** Collects a sequence of [[EType embeddable types]] into a [[ETypePool]].
   * @param embeddableTypes the sequence of embeddable types stored in the pool
   * @throws longevity.exceptions.subdomain.DuplicateETypesException when two
   * `ETypes` have the same `Embeddable` type
   */
  @throws[DuplicateETypesException]("when two ETypes have the same Embeddable type")
  def apply(eTypes: EType[_ <: Embeddable]*): ETypePool = {
    val map: TypeKeyMap[Embeddable, EType] = eTypes.foldLeft(TypeKeyMap[Embeddable, EType]()) {
      case (map, eType) => map + (eType.eTypeKey -> eType)
    }
    if (eTypes.size != map.size) throw new DuplicateETypesException
    map
  }

  /** an empty embeddable type pool */
  val empty: ETypePool = apply()

}
