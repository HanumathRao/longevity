package longevity.subdomain

import com.github.nscala_time.time.Imports._
import emblem.imports._
import longevity.exceptions.InvalidNatKeyPropPathException
import org.scalatest._
import longevity.integration.master._

/** unit tests for the proper construction of [[RootEntityType#NatKeyProp nat key props]] */
@longevity.UnitTest
class NatKeyPropSpec extends FlatSpec with GivenWhenThen with Matchers {

  behavior of "RootEntityType.NatKeyProp.apply(String)"

  it should "throw InvalidNatKeyPropPathException when the specified prop path is empty" in {
    intercept[InvalidNatKeyPropPathException] {
      AllAttributes.NatKeyProp("")
    }
  }

  it should "throw InvalidNatKeyPropPathException when the specified prop path does not map " +
  "to an actual prop path" in {
    intercept[InvalidNatKeyPropPathException] {
      AllAttributes.NatKeyProp("invalidPropPath")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponent.NatKeyProp("component.noSuchPathSegment")
    }
  }

  it should "throw InvalidNatKeyPropPathException when the specified prop path passes through a collection" in {
    intercept[InvalidNatKeyPropPathException] {
      WithComponentList.NatKeyProp("components.uri")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponentOption.NatKeyProp("component.uri")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponentSet.NatKeyProp("components.uri")
    }
  }

  it should "throw InvalidNatKeyPropPathException when the specified prop path terminates with a collection" in {
    intercept[InvalidNatKeyPropPathException] {
      AttributeLists.NatKeyProp("boolean")
    }

    intercept[InvalidNatKeyPropPathException] {
      AttributeOptions.NatKeyProp("boolean")
    }

    intercept[InvalidNatKeyPropPathException] {
      AttributeSets.NatKeyProp("boolean")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithAssocList.NatKeyProp("associated")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithAssocOption.NatKeyProp("associated")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithAssocSet.NatKeyProp("associated")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponentList.NatKeyProp("components")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponentOption.NatKeyProp("component")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponentSet.NatKeyProp("components")
    }

    intercept[InvalidNatKeyPropPathException] {
      WithComponent.NatKeyProp("component.tags")
    }
  }

  it should "produce a valid nat key prop for basic types" in {
    var prop: AllAttributes.NatKeyProp = null

    prop = AllAttributes.NatKeyProp("boolean")
    prop.path should equal ("boolean")
    prop.typeKey should equal (typeKey[Boolean])

    prop = AllAttributes.NatKeyProp("char")
    prop.path should equal ("char")
    prop.typeKey should equal (typeKey[Char])

    prop = AllAttributes.NatKeyProp("double")
    prop.path should equal ("double")
    prop.typeKey should equal (typeKey[Double])

    prop = AllAttributes.NatKeyProp("float")
    prop.path should equal ("float")
    prop.typeKey should equal (typeKey[Float])

    prop = AllAttributes.NatKeyProp("int")
    prop.path should equal ("int")
    prop.typeKey should equal (typeKey[Int])

    prop = AllAttributes.NatKeyProp("long")
    prop.path should equal ("long")
    prop.typeKey should equal (typeKey[Long])

    prop = AllAttributes.NatKeyProp("string")
    prop.path should equal ("string")
    prop.typeKey should equal (typeKey[String])

    prop = AllAttributes.NatKeyProp("dateTime")
    prop.path should equal ("dateTime")
    prop.typeKey should equal (typeKey[DateTime])
  }

  it should "produce a valid nat key prop for shorthand types" in {
    var prop: AllShorthands.NatKeyProp = null

    prop = AllShorthands.NatKeyProp("boolean")
    prop.path should equal ("boolean")
    prop.typeKey should equal (typeKey[BooleanShorthand])

    prop = AllShorthands.NatKeyProp("char")
    prop.path should equal ("char")
    prop.typeKey should equal (typeKey[CharShorthand])

    prop = AllShorthands.NatKeyProp("double")
    prop.path should equal ("double")
    prop.typeKey should equal (typeKey[DoubleShorthand])

    prop = AllShorthands.NatKeyProp("float")
    prop.path should equal ("float")
    prop.typeKey should equal (typeKey[FloatShorthand])

    prop = AllShorthands.NatKeyProp("int")
    prop.path should equal ("int")
    prop.typeKey should equal (typeKey[IntShorthand])

    prop = AllShorthands.NatKeyProp("long")
    prop.path should equal ("long")
    prop.typeKey should equal (typeKey[LongShorthand])

    prop = AllShorthands.NatKeyProp("string")
    prop.path should equal ("string")
    prop.typeKey should equal (typeKey[StringShorthand])

    prop = AllShorthands.NatKeyProp("dateTime")
    prop.path should equal ("dateTime")
    prop.typeKey should equal (typeKey[DateTimeShorthand])
  }

  it should "produce a valid nat key prop for an assoc" in {
    val prop = WithAssoc.NatKeyProp("associated")
    prop.path should equal ("associated")
    prop.typeKey should equal (typeKey[Assoc[Associated]])
  }

  it should "produce a valid nat key prop for a nested basic type" in {
    val prop = WithComponent.NatKeyProp("component.uri")
    prop.path should equal ("component.uri")
    prop.typeKey should equal (typeKey[String])
  }

  it should "produce a valid nat key prop for shorthand types in nested components" in {
    var prop: WithComponentWithShorthands.NatKeyProp = null

    prop = WithComponentWithShorthands.NatKeyProp("component.boolean")
    prop.path should equal ("component.boolean")
    prop.typeKey should equal (typeKey[BooleanShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.char")
    prop.path should equal ("component.char")
    prop.typeKey should equal (typeKey[CharShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.double")
    prop.path should equal ("component.double")
    prop.typeKey should equal (typeKey[DoubleShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.float")
    prop.path should equal ("component.float")
    prop.typeKey should equal (typeKey[FloatShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.int")
    prop.path should equal ("component.int")
    prop.typeKey should equal (typeKey[IntShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.long")
    prop.path should equal ("component.long")
    prop.typeKey should equal (typeKey[LongShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.string")
    prop.path should equal ("component.string")
    prop.typeKey should equal (typeKey[StringShorthand])

    prop = WithComponentWithShorthands.NatKeyProp("component.dateTime")
    prop.path should equal ("component.dateTime")
    prop.typeKey should equal (typeKey[DateTimeShorthand])
  }

  it should "produce a valid nat key prop for a nested assoc" in {
    val prop = WithComponentWithAssoc.NatKeyProp("component.associated")
    prop.path should equal ("component.associated")
    prop.typeKey should equal (typeKey[Assoc[Associated]])
  }

}
