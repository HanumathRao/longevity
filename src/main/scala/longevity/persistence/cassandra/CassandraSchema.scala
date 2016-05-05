package longevity.persistence.cassandra

import com.datastax.driver.core.exceptions.InvalidQueryException
import emblem.TypeKey
import emblem.typeKey
import longevity.subdomain.Assoc
import longevity.subdomain.persistent.Persistent
import longevity.subdomain.ptype.Prop

/** implementation of CassandraRepo.createSchema */
private[cassandra] trait CassandraSchema[P <: Persistent] {
  repo: CassandraRepo[P] =>

  protected def createSchema(): Unit = {
    createTable()
    createRealizedPropColumns()
    createIndexes()
  }

  protected def createTable(): Unit = {
    val createTable = s"""|
    |CREATE TABLE IF NOT EXISTS $tableName (
    |  id uuid,
    |  p text,
    |  PRIMARY KEY (id)
    |)
    |WITH COMPRESSION = { 'sstable_compression': 'SnappyCompressor' };
    |""".stripMargin
    session.execute(createTable)
  }

  protected def createRealizedPropColumns(): Unit = {
    realizedProps.map { prop =>
      addColumn(columnName(prop), typeKeyToCassandraType(prop.propTypeKey))
    }
  }

  protected def addColumn(columnName: String, columnType: String): Unit = {
    val cql = s"ALTER TABLE $tableName ADD $columnName $columnType"
    try {
      session.execute(cql)
    } catch {
      case e: InvalidQueryException
        if e.getMessage.contains("because it conflicts with an existing column") =>
        // ignoring this exception is recommended ALTER TABLE ADD IF NOT EXISTS
        // http://stackoverflow.com/questions/25728944/cassandra-add-column-if-not-exists
    }
  }

  protected def typeKeyToCassandraType[A](key: TypeKey[A]): String = {
    if (key <:< typeKey[Assoc[_ <: Persistent]]) {
      "uuid"
    } else if (CassandraRepo.basicToCassandraType.contains(key)) {
      CassandraRepo.basicToCassandraType(key)
    } else if (shorthandPool.contains(key)) {
      CassandraRepo.basicToCassandraType(shorthandPool(key).abbreviatedTypeKey)
    } else {
      throw new RuntimeException(s"unexpected prop type ${key.tpe}")
    }
  }

  protected def createIndexes(): Unit = realizedProps.foreach(createIndex)

  protected def createIndex(prop: Prop[P, _]): Unit = {
    val name = s"""${tableName}_${scoredPath(prop)}"""
    val createIndex = s"CREATE INDEX IF NOT EXISTS $name ON $tableName (${columnName(prop)});"
    session.execute(createIndex)
  }

}
