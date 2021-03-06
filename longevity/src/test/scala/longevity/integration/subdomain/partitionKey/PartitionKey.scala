package longevity.integration.subdomain.partitionKey

import longevity.subdomain.Persistent
import longevity.subdomain.PType

// NOTE unfortunate name clash here with longevity.subdomain.ptype.PartitionKey
case class PartitionKey(key: Key) extends Persistent

object PartitionKey extends PType[PartitionKey] {
  object props {
    val key = prop[Key]("key")
  }
  object keys {
    val key = partitionKey(props.key)
  }
}
