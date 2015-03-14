package longevity.repo

import emblem._
import longevity.domain._

object testUtil {

  case class Friend(name: String) extends RootEntity

  object FriendType extends RootEntityType[Friend]

  case class Post(author: Assoc[Friend], content: String) extends RootEntity

  object PostType extends RootEntityType[Post]

  val entityTypes = EntityTypePool() + FriendType + PostType

  val shorthands = ShorthandPool()

  val boundedContext = BoundedContext("blog", entityTypes, shorthands)

}
