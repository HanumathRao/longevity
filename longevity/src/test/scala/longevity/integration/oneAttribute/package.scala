package longevity.integration

import emblem._
import longevity.context._
import longevity.domain._

/** covers a root entity with a single attribute */
package object oneAttribute {

  val entityTypes = EntityTypePool() + OneAttribute

  val subdomain = Subdomain("One Attribute", entityTypes)

  val boundedContext = BoundedContext(Mongo, subdomain)

}
