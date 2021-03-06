package longevity.integration.duplicateKeyVal

import longevity.TestLongevityConfigs
import longevity.context.LongevityContext
import longevity.exceptions.persistence.DuplicateKeyValException
import longevity.integration.subdomain.partitionKey.Key
import longevity.integration.subdomain.partitionKey.PartitionKey
import longevity.integration.subdomain.partitionKey.subdomain
import longevity.persistence.Repo
import longevity.test.LongevityFuturesSpec
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.scalatest.GivenWhenThen
import scala.concurrent.ExecutionContext.{ global => globalExecutionContext }

/** expect all back ends to throw DuplicateKeyValException on a duplicate
 * partition key value. except cassandra of course. in cassandra, if you insert
 * a duplicate primary key, it will simply overwrite the old row.
 */
class DuplicatePartitionKeyValSpec extends FlatSpec with LongevityFuturesSpec
with BeforeAndAfterAll
with GivenWhenThen {

  override protected implicit val executionContext = globalExecutionContext

  val mongoContext = new LongevityContext(subdomain, TestLongevityConfigs.mongoConfig)

  override def beforeAll() = mongoContext.testRepoPool.createSchema().futureValue

  assertDuplicateKeyValBehavior(mongoContext.inMemTestRepoPool[PartitionKey], "InMemRepo")
  assertDuplicateKeyValBehavior(mongoContext.testRepoPool[PartitionKey], "MongoRepo")

  def assertDuplicateKeyValBehavior(repo: Repo[PartitionKey], repoName: String): Unit = {

    behavior of s"$repoName.create"

    it should "throw exception on attempt to insert duplicate key val for a partition key" in {

      val origKey = Key("orig")
      val p1 = PartitionKey(origKey)
      val s1 = repo.create(p1).futureValue

      try {
        val exception = repo.create(p1).failed.futureValue
        if (!exception.isInstanceOf[DuplicateKeyValException[_]]) {
          exception.printStackTrace
        }
        exception shouldBe a [DuplicateKeyValException[_]]

        val dkve = exception.asInstanceOf[DuplicateKeyValException[PartitionKey]]
        dkve.p should equal (p1)
        (dkve.key: AnyRef) should equal (PartitionKey.keys.key)
      } finally {
        repo.delete(s1).futureValue
      }
    }

  }

}
