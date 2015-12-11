package longevity.integration.subdomain

import longevity.context._
import longevity.subdomain._

/** covers a root entity with a list of component entities */
package object withComponentList {

  implicit val shorthandPool = ShorthandPool.empty

  object context {
    val entityTypes = EntityTypePool() + WithComponentList + Component
    val subdomain = Subdomain("With Component List", entityTypes)
    val mongoContext = LongevityContext(subdomain, Mongo)
  }

}