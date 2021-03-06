package longevity.integration.subdomain.indexWithMultipleProperties

import longevity.subdomain.Persistent
import longevity.subdomain.PType

case class IndexWithMultipleProperties(
  realm: String,
  name: String)
extends Persistent

object IndexWithMultipleProperties extends PType[IndexWithMultipleProperties] {
  object props {
    val realm = prop[String]("realm")
    val name = prop[String]("name")
  }
  object keys {
  }
  object indexes {
    val realmName = index(props.realm, props.name)
  }
}
