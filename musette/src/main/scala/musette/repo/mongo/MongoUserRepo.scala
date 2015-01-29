package musette.repo.mongo

import longevity.repo._
import musette.domain._
import musette.repo.UserRepo

class MongoUserRepo(implicit repoPool: RepoPool)
extends MusetteMongoRepo[User](UserType)
with UserRepo