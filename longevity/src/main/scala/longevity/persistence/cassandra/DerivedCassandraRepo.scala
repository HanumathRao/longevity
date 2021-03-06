package longevity.persistence.cassandra

import longevity.persistence.PState
import longevity.subdomain.Persistent
import longevity.subdomain.realized.RealizedPropComponent
import longevity.subdomain.realized.RealizedKey
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.blocking

private[cassandra] trait DerivedCassandraRepo[P <: Persistent, Poly >: P <: Persistent] extends CassandraRepo[P] {

  protected val polyRepo: CassandraRepo[Poly]

  override protected[cassandra] val tableName: String = polyRepo.tableName

  override protected def jsonStringForP(p: P): String = {
    // we use the poly type key here so we get the discriminator in the JSON
    import org.json4s.native.JsonMethods._
    compact(render(emblematicToJsonTranslator.translate[Poly](p)(polyRepo.pTypeKey)))
  }

  override protected[cassandra] def indexedComponents: Set[RealizedPropComponent[_ >: P <: Persistent, _, _]] = {
    myIndexedComponents ++ polyRepo.indexedComponents
  }

  private def myIndexedComponents = super.indexedComponents

  override protected[persistence] def createSchema()(implicit context: ExecutionContext)
  : Future[Unit] = Future {
    blocking {
      createActualizedPropColumns()
      createIndexes()
    }
  }

  private def createActualizedPropColumns(): Unit = {
    actualizedComponents.map {
      prop => addColumn(columnName(prop), componentToCassandraType(prop))
    }
  }

  override protected def updateColumnNames(isCreate: Boolean = true): Seq[String] = {
    super.updateColumnNames(isCreate) :+ "discriminator"
  }

  override protected def updateColumnValues(state: PState[P], isCreate: Boolean = true): Seq[AnyRef] = {
    val discriminatorValue = state.get.getClass.getSimpleName
    super.updateColumnValues(state, isCreate) :+ discriminatorValue
  }

  override protected def keyValSelectStatementConjunction(key: RealizedKey[P, _]): String =
    super.keyValSelectStatementConjunction(key) + s"\nAND\n  discriminator = '$discriminatorValue'"

  override protected def queryWhereClause(filterInfo: FilterInfo): String =
    super.queryWhereClause(filterInfo) + s"\nAND\n  discriminator = '$discriminatorValue'"

  override protected[cassandra] def deleteStatement = polyRepo.deleteStatement

  private def discriminatorValue = pTypeKey.name

}
