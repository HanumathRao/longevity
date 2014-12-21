package musette
package repo.inmem

import org.scalatest._
import org.scalatest.OptionValues._
import longevity.testUtils.InMemRepoSpec
import domain.testUtils._
import domain.User

class InMemUserRepoSpec extends InMemRepoSpec[User] {

  private val repoLayer = new InMemRepoLayer
  def entityTypeName = "user"
  def repo = repoLayer.userRepo
  def genTestEntity = domain.testUtils.testEntityGen.user _
  def updateTestEntity = { e => e.copy(uri = e.uri + "77") }
  def persistedShouldMatchUnpersisted = entityMatchers.persistedUserShouldMatchUnpersisted _

}

