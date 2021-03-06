package longevity.ddd.subdomain

import emblem.TypeKey
import longevity.subdomain.PType

/** a type class for events */
abstract class EventType[E <: Event](implicit eventTypeKey: TypeKey[E])
extends PType[E]()(eventTypeKey)
