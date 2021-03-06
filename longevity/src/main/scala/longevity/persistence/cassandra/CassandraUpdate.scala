package longevity.persistence.cassandra

import com.datastax.driver.core.BoundStatement
import longevity.exceptions.persistence.WriteConflictException
import longevity.persistence.PState
import longevity.subdomain.Persistent
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.blocking

/** implementation of CassandraRepo.update */
private[cassandra] trait CassandraUpdate[P <: Persistent] {
  repo: CassandraRepo[P] =>

  override def update(state: PState[P])(implicit context: ExecutionContext): Future[PState[P]] =
    Future {
      logger.debug(s"calling CassandraRepo.update: $state")
      validateStablePartitionKey(state)
      val newState = state.update(persistenceConfig.optimisticLocking)
      val resultSet = blocking {
        session.execute(bindUpdateStatement(newState, state.rowVersionOrNull))
      }
      if (persistenceConfig.optimisticLocking) {
        val updateSuccess = resultSet.one.getBool(0)
        if (!updateSuccess) {
          throw new WriteConflictException(state)
        }
      }
      logger.debug(s"done calling CassandraRepo.update: $newState")
      newState
    }

  private def bindUpdateStatement(state: PState[P], rowVersion: AnyRef): BoundStatement = {
    val columnBindings = if (persistenceConfig.optimisticLocking) {
      updateColumnValues(state, isCreate = false) ++: whereBindings(state) :+ rowVersion
    } else {
      updateColumnValues(state, isCreate = false) ++: whereBindings(state)
    }
    logger.debug(s"invoking CQL: ${updateStatement.getQueryString} with bindings: $columnBindings")
    updateStatement.bind(columnBindings: _*)
  }

  private lazy val updateStatement = preparedStatement(updateCql)

  private def updateCql = if (persistenceConfig.optimisticLocking) {
    withLockingUpdateCql
  } else {
    withoutLockingUpdateCql
  }

  private def columnAssignments = updateColumnNames(isCreate = false).map(c => s"$c = :$c").mkString(",\n  ")

  private def withoutLockingUpdateCql = s"""|
  |UPDATE $tableName
  |SET
  |  $columnAssignments
  |WHERE
  |  $whereAssignments
  |""".stripMargin

  private def withLockingUpdateCql = s"""|$withoutLockingUpdateCql
  |IF
  |  row_version = :row_version
  |""".stripMargin

}
