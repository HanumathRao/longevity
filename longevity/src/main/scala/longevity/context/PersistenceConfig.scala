package longevity.context

/** configuration for persistence that is [[BackEnd back end]] agnostic */
private[longevity] trait PersistenceConfig {

  /** should longevity autocreate schema when the repositories are created? */
  val autocreateSchema: Boolean

  /** is optimistic locking turned on? */
  val optimisticLocking: Boolean

}
