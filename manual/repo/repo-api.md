---
title: the repo api
layout: page
---

The API for a longevity repository provides basic CRUD persistence
operations, as follows:

```scala
package longevity.persistence

import longevity.subdomain.KeyVal
import longevity.subdomain.persistent.Persistent
import longevity.subdomain.ptype.Query
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/** a repository for persistent objects of type `P` */
trait Repo[P <: Persistent] {

  /** creates the persistent object
   * 
   * @param unpersisted the persistent object to create
   * @param executionContext the execution context
   */
  def create(unpersisted: P)(implicit executionContext: ExecutionContext): Future[PState[P]]

  /** retrieves an optional persistent object from a
   * [[longevity.subdomain.KeyVal key value]]
   *
   * @tparam V the type of the key value
   * @param keyVal the key value to use to look up the persistent object
   * @param executionContext the execution context
   */
  def retrieve[V <: KeyVal[P, V]](keyVal: V)(implicit executionContext: ExecutionContext)
  : Future[Option[PState[P]]]

  /** retrieves an optional persistent object from a
   * [[longevity.subdomain.KeyVal key value]]
   * 
   * throws NoSuchElementException whenever the persistent ref does not refer
   * to a persistent object in the repository
   * 
   * @tparam V the type of the key value
   * @param keyVal the key value to use to look up the persistent object
   * @param executionContext the execution context
   */
  def retrieveOne[V <: KeyVal[P, V]](keyVal: V)(implicit executionContext: ExecutionContext)
  : Future[PState[P]]

  /** retrieves multiple persistent objects matching a query
   * 
   * @param query the query to execute
   * @param executionContext the execution context
   */
  def retrieveByQuery(query: Query[P])(implicit executionContext: ExecutionContext): Future[Seq[PState[P]]]

  /** updates the persistent object
   * 
   * @param state the persistent state of the persistent object to update
   * @param executionContext the execution context
   */
  def update(state: PState[P])(implicit executionContext: ExecutionContext): Future[PState[P]]

  /** deletes the persistent object
   * 
   * @param state the persistent state of the persistent object to delete
   * @param executionContext the execution context
   */
  def delete(state: PState[P])(implicit executionContext: ExecutionContext): Future[Deleted[P]]

}
```

Don't worry about the complicated type parameters on the `retrieve`
and `retrieveOne` methods - they can easily be inferred by the compiler.

Methods returning a Scala `Future` require an implicit execution
context argument. The easiest way to provide this is to include
`import scala.concurrent.ExecutionContext.Implicits.global` at the top
of the file.

The streaming query API is provided by an implicit conversion from
`Repo[P]` to `StreamingRepo[P]`, which looks something like this:

```scala
package longevity.persistence

import akka.NotUsed
import akka.stream.scaladsl.Source
import longevity.subdomain.ptype.Query
import longevity.subdomain.persistent.Persistent

/** provides repository methods that use Akka Streams for repository streaming
 * API.
 * 
 * `StreamingRepo` is provided by an implicit conversion from `Repo`, so that
 * Akka Streams can remain an optional dependency for longevity users.
 * otherwise, it would have been included as part of the [[Repo]].
 */
implicit class StreamingRepo[P <: Persistent](repo: Repo[P]) {

  /** streams persistent objects matching a query
   * 
   * @param query the query to execute
   */
  def streamByQuery(query: Query[P]): Source[PState[P], NotUsed] =
      repo.asInstanceOf[BaseRepo[P]].streamByQueryImpl(query)

}
```

The `streamByQuery` method is provided implicitly, rather than
directly within the `Repo` API, so that Akka streams can be an
optional dependency.

We will will discuss the `Repo` API methods in turn, but it's helpful
to cover one point up front: the `retrieveOne` method is a simple
wrapper method for `retrieve`, that opens up the `Option[PState[R]]`
for you. If the option is a `None`, this will result in a
`NoSuchElementException`.

{% assign prevTitle = "repositories" %}
{% assign prevLink = "." %}
{% assign upTitle = "repositories" %}
{% assign upLink = "." %}
{% assign nextTitle = "repo.create" %}
{% assign nextLink = "create.html" %}
{% include navigate.html %}