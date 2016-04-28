package emblem

/** describes a collection of types used in composite data structures. these
 * data structures can contain all of the following types:
 * 
 * - all of the [[basicTypes basic types]]
 * - supported collections. currently `Option`, `Set`, and `Map`
 * - case classes represented in the collection of [[Emblem emblems]]
 * - case classes represented in the collection of [[Extractor extractors]]
 * - traits represented in the collection of [[Union unions]]
 *
 * @param extractors the extractors to use in the emblematic. defaults to empty
 * @param emblems the emblems to use in the emblematic. defaults to empty
 * @param unions the unions to use in the emblematic. defaults to empty
 */
case class Emblematic(
  extractors: ExtractorPool = ExtractorPool.empty,
  emblems: EmblemPool = EmblemPool.empty,
  unions: UnionPool = UnionPool.empty) {

  /** a collection of all the emblems and unions */
  lazy val reflectives =
    TypeKeyMap[Any, Reflective] ++ emblems.widen[Reflective] ++ unions.widen[Reflective]

}

object Emblematic {

  /** an empty emblematic */
  val empty = Emblematic()

}
