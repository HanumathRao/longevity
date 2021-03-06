package longevity.integration.queries.mongo

import longevity.TestLongevityConfigs
import longevity.context.LongevityContext
import longevity.integration.subdomain.foreignKey._
import longevity.test.QuerySpec
import scala.concurrent.ExecutionContext.Implicits.global

class WithForeignKeyQuerySpec extends QuerySpec[WithForeignKey](
  new LongevityContext(subdomain, TestLongevityConfigs.mongoConfig)) {

  lazy val sample = randomP

  val idProp = WithForeignKey.props.id
  val associatedProp = WithForeignKey.props.associated

  import WithForeignKey.queryDsl._

  behavior of "InMemRepo.retrieveByQuery"
  it should "produce expected results for simple equality queries with foreign keys" in {
    exerciseQuery(associatedProp eqs sample.associated)
    exerciseQuery(associatedProp neq sample.associated)
  }

  behavior of "InMemRepo.retrieveByQuery"
  it should "produce expected results for simple conditional queries" in {
    exerciseQuery(idProp eqs sample.id or associatedProp eqs sample.associated)
    exerciseQuery(idProp lt sample.id and associatedProp neq sample.associated)
  }

}
