package longevity.integration.subdomain.simpleConstraint

import longevity.subdomain.Persistent
import longevity.subdomain.PType

case class SimpleConstraint(
  id: SimpleConstraintId,
  primaryEmail: Email,
  emails: Set[Email])
extends Persistent

object SimpleConstraint extends PType[SimpleConstraint] {
  object props {
    val id = prop[SimpleConstraintId]("id")    
  }
  object keys {
    val id = key(props.id)
  }
}
