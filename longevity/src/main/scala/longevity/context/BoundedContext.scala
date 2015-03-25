package longevity.context

import longevity.repo.SpecializedRepoFactoryPool
import longevity.repo.emptySpecializedRepoFactoryPool
import longevity.repo.repoPoolForBoundedContext
import longevity.domain.Subdomain
import emblem.ShorthandPool
import emblem.traversors.Generator.CustomGenerators
import emblem.traversors.Generator.emptyCustomGenerators

/** the bounded context of your subdomain. this is a capture of the strategies and tools used by the applications
 * relating to your subdomain
 *
 * @tparam PS the kind of persistence strategy for this bounded context
 * @param subdomain The subdomain
 * @param shorthandPool a complete set of the shorthands used by the domain
 * @param specializations a collection factories for specialized repositories
 * @param customGenerators a collection of custom generators to use when generating test data. defaults to an
 * empty collection.
 * @param persistenceStrategy the persistence strategy for this bounded context
 */
case class BoundedContext(
  persistenceStrategy: PersistenceStrategy,
  subdomain: Subdomain,
  shorthandPool: ShorthandPool = ShorthandPool(),
  specializations: SpecializedRepoFactoryPool = emptySpecializedRepoFactoryPool,
  customGenerators: CustomGenerators = emptyCustomGenerators) {

  /** The standard set of repositories for this bounded context */
  lazy val repoPool = repoPoolForBoundedContext(this)

  /** An in-memory set of repositories for this bounded context, for use in testing. no specializations are
   * provided. */
  lazy val inMemRepoPool = longevity.repo.inMemRepoPool(this)

  // TODO scaladoc
  class RepoPoolSpec extends longevity.testUtil.RepoPoolSpec(
    this,
    this.repoPool,
    suiteNameSuffix = Some("(Mongo)"))

  // TODO scaladoc
  class InMemRepoPoolSpec extends longevity.testUtil.RepoPoolSpec(
    this,
    inMemRepoPool,
    suiteNameSuffix = Some("(InMem)"))

}
