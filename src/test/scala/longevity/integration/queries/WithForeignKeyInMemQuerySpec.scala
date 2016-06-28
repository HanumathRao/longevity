package longevity.integration.queries

import longevity.test.QuerySpec
import longevity.integration.subdomain.withForeignKey._
import scala.concurrent.ExecutionContext.Implicits.global

class WithForeignKeyInMemQuerySpec extends QuerySpec[WithForeignKey](
  mongoContext,
  mongoContext.inMemTestRepoPool) {

  lazy val sample = randomP

  val idProp = WithForeignKey.props.id
  val associatedProp = WithForeignKey.props.associated

  import WithForeignKey.queryDsl._

  behavior of "InMemRepo.retrieveByQuery"
  it should "produce expected results for simple equality queries with associations" in {
    exerciseQuery(associatedProp eqs sample.associated)
    exerciseQuery(associatedProp neq sample.associated)
  }

  behavior of "InMemRepo.retrieveByQuery"
  it should "produce expected results for simple conditional queries" in {
    exerciseQuery(idProp eqs sample.id or associatedProp eqs sample.associated)
    exerciseQuery(idProp lt sample.id and associatedProp neq sample.associated)
  }

}