package longevity.integration.subdomain.component

import org.scalatest.Suites
import scala.concurrent.ExecutionContext.Implicits.global

class WithComponentSpec extends Suites(contexts.map(_.repoCrudSpec): _*)
