package longevity

import scala.reflect.runtime.universe.TypeTag

/** Provides support for constructing your domain.
  * 
  * Ideally, you wouldn't actually need to use any of the classes here. I.e.,
  * [[longevity.repo]] should not depend on anything here.
  * They should just be tools for the user to use if she chooses to.
  * But we'll see how that turns out.
  */
package object domain
