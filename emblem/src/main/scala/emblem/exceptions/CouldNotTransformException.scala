package emblem.exceptions

import emblem.TypeKey

/** an exception thrown when an [[emblem.emblematic.traversors.async.Transformer
 * asynchronous Transformer]] or a
 * [[emblem.emblematic.traversors.sync.Transformer synchronous Transformer]]
 * cannot transform requested data due to encountering an unsupported type
 */
class CouldNotTransformException(val typeKey: TypeKey[_], cause: CouldNotTraverseException)
extends TraversorException(s"don't know how to transform type ${typeKey.tpe}", cause)
