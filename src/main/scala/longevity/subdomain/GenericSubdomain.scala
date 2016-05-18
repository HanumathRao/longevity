package longevity.subdomain

import longevity.subdomain.entity.EntityTypePool
import longevity.subdomain.ptype.PTypePool

object GenericSubdomain {

  /** constructs a generic subdomain. really just another name for a [[Subdomain]].
   *
   * @param name the name of the core domain
   * @param pTypePool a complete set of the persistent types in the subdomain.
   * defaults to empty
   * @param entityTypePool a complete set of the entity types within the core
   * domain. defaults to empty
   * @param shorthandPool a complete set of the shorthands used by the bounded
   * context. defaults to empty
   */
  def apply(
    name: String,
    pTypePool: PTypePool = PTypePool.empty,
    entityTypePool: EntityTypePool = EntityTypePool.empty,
    shorthandPool: ShorthandPool = ShorthandPool.empty)
  : GenericSubdomain = 
    Subdomain(name, pTypePool, entityTypePool, shorthandPool)

}