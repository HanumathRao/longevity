package emblem.exceptions

import emblem.TypeKey

/** an exception thrown when [[emblem.traversors.Transformer]] cannot transform requested data due to
 * encountering an unsupported type.
 */
class CouldNotTransformException(val typeKey: TypeKey[_], cause: CouldNotTraverseException)
extends TraversorException(s"don't know how to transform type ${typeKey.tpe}", cause)
