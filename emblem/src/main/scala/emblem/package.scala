import scala.reflect.runtime.universe.TypeTag

/** a collection of utilities for reflecting on types */
package object emblem {

  /** returns a [[TypeKey]] for the specified type `A`. this method will only work where a `TypeTag` is
   * implicitly available.
   */
  def typeKey[A : TypeKey]: TypeKey[A] = implicitly[TypeKey[A]]

  /** an implicit method for producing a [[TypeKey]]. this method allows type keys to be available implicitly
   * anywhere that the corresponding `TypeTag` is implicitly available.
   */
  implicit def typeKeyFromTag[A : TypeTag]: TypeKey[A] = TypeKey(implicitly[TypeTag[A]])

  // TODO pt-86950564: an extension class for Map with a toTypeKeyMap[B,V] method
  // TODO pt-86950588: an extension class for Map with a toTypeBoundMap[B,K,V] method

}
