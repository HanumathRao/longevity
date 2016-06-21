package longevity.integration.queries

import longevity.test.QuerySpec
import longevity.integration.subdomain.oneShorthand._
import scala.concurrent.ExecutionContext.Implicits.global

class OneShorthandMongoQuerySpec extends QuerySpec[OneShorthand](mongoContext, mongoContext.testRepoPool) {

  lazy val sample = randomP

  val uriProp = OneShorthand.prop[Uri]("uri")

  import OneShorthand.queryDsl._

  behavior of "MongoRepo.retrieveByQuery"

  it should "produce expected results for simple equality queries" in {
    exerciseQuery(uriProp eqs sample.uri)
    exerciseQuery(uriProp neq sample.uri)
  }

  it should "produce expected results for simple ordering queries" in {
    exerciseQuery(uriProp gt sample.uri)
    exerciseQuery(uriProp gte sample.uri)
    exerciseQuery(uriProp lt sample.uri)
    exerciseQuery(uriProp lte sample.uri)
  }

}