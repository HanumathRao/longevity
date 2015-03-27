package longevity.integration

import emblem._
import longevity.context._
import longevity.persistence._
import longevity.subdomain._

/** covers a root entity with shorthands for every supported basic type */
package object allShorthands {

  val entityTypes = EntityTypePool() + AllShorthands

  val subdomain = Subdomain("All Shorthands", entityTypes)

  val booleanShorthand = shorthandFor[BooleanShorthand, Boolean]
  val charShorthand = shorthandFor[CharShorthand, Char]
  val doubleShorthand = shorthandFor[DoubleShorthand, Double]
  val floatShorthand = shorthandFor[FloatShorthand, Float]
  val intShorthand = shorthandFor[IntShorthand, Int]
  val longShorthand = shorthandFor[LongShorthand, Long]
  val stringShorthand = shorthandFor[StringShorthand, String]

  val shorthandPool = ShorthandPool() +
    booleanShorthand +
    charShorthand +
    doubleShorthand +
    floatShorthand +
    intShorthand +
    longShorthand +
    stringShorthand

  val longevityContext = LongevityContext(subdomain, shorthandPool, Mongo)

}
