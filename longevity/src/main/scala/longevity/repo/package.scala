package longevity

import emblem._
import domain.Entity
import domain.EntityType
import domain.BoundedContext

// TODO: package level scaladoc
package object repo {

  /** a `TypeKeyMap` of [[domain.Entity Entity]] to [[Repo]] */
  type RepoPool = TypeKeyMap[Entity, Repo]  

  /** like a [[RepoPool]], except that the [[Repo repositories]] have not yet been fully initialized */
  type ProvisionalRepoPool = TypeKeyMap[Entity, Repo]  

  /** an empty [[ProvisionalRepoPool]] */
  val emptyProvisionalRepoPool = TypeKeyMap[Entity, Repo]

  /** builds and returns a [[RepoPool]] of [[InMemRepo in-memory repositories]] for all the entities in a
   * bounded context. stock in-memory repositories will created, except where specialized versions are
   * provided.
   * @param boundedContext the bounded context
   * @param specializations specialized repositories to include in the pool, in place of the stock
   * in-memory repositories
   */
  def inMemRepoPool(
    boundedContext: BoundedContext,
    specializations: ProvisionalRepoPool = emptyProvisionalRepoPool)
  : RepoPool = {
    object repoFactory extends stock.RepoFactory {
      def build[E <: Entity](entityType: EntityType[E], entityKey: TypeKey[E]): Repo[E] =
        new InMemRepo(entityType)(entityKey)
    }
    buildRepoPool(boundedContext, repoFactory, specializations)
  }

  /** builds and returns a [[RepoPool]] of [[MongoRepo mongo repositories]] for all the entities in a
   * bounded context. stock mongo repositories will created, except where specialized versions are
   * provided.
   * @param boundedContext the bounded context
   * @param specializations specialized repositories to include in the pool, in place of the stock
   * mongo repositories
   */
  def mongoRepoPool(
    boundedContext: BoundedContext,
    specializations: ProvisionalRepoPool = emptyProvisionalRepoPool)
  : RepoPool = {
    object repoFactory extends stock.RepoFactory {
      def build[E <: Entity](entityType: EntityType[E], entityKey: TypeKey[E]): Repo[E] =
        new MongoRepo(entityType, boundedContext.shorthandPool)(entityKey)
    }
    buildRepoPool(boundedContext, repoFactory, specializations)
  }

  // RepoFactory is inside object stock to prevent lint warning about declaring classes in package objects
  private object stock {
    trait RepoFactory {
      def build[E <: Entity](entityType: EntityType[E], entityKey: TypeKey[E]): Repo[E]
    }
  }

  private def buildRepoPool(
    boundedContext: BoundedContext,
    stockRepoFactory: stock.RepoFactory,
    specializations: ProvisionalRepoPool)
  : RepoPool = {
    var repoPool = emptyRepoPool
    def createRepoFromPair[E <: Entity](pair: TypeBoundPair[Entity, TypeKey, EntityType, E]): Unit = {
      val entityKey = pair._1
      val entityType = pair._2
      val repo = specializations.get(entityKey) match {
        case Some(repo) => repo
        case None => stockRepoFactory.build(entityType, entityKey)
      }
      repoPool += (entityKey -> repo)
    }
    boundedContext.entityTypePool.iterator.foreach { pair => createRepoFromPair(pair) }
    finishRepoInitialization(repoPool)
    repoPool
  }

  private val emptyRepoPool = TypeKeyMap[Entity, Repo]

  private def finishRepoInitialization(repoPool: RepoPool): Unit = {
    repoPool.values.foreach { repo => repo._repoPoolOption = Some(repoPool) }
  }

}