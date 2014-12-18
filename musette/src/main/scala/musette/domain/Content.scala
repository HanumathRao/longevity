package musette.domain

import longevity.domain._

/** content authored by a site user. */
trait Content {
  val uri: Uri
  val authors: Set[Assoc[User]]
  val content: Markdown
}
