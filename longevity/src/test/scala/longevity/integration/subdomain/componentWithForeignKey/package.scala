package longevity.integration.subdomain

import longevity.TestLongevityConfigs
import longevity.subdomain.ETypePool
import longevity.subdomain.EType
import longevity.subdomain.Subdomain
import longevity.subdomain.PTypePool

/** covers a persistent with a single component entity with an association to another persistent */
package object componentWithForeignKey {

  val subdomain = Subdomain(
    "Component With Foreign Key",
    PTypePool(WithComponentWithForeignKey, Associated),
    ETypePool(
      EType[ComponentWithForeignKey]))

  val contexts = TestLongevityConfigs.sparseContextMatrix(subdomain)

}
