import org.specs.Specification
import org.specs.ScalaCheck
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary.arbitrary
import scala.collection.JavaConversions._

trait PeopleGen {
  val femaleNameGen = oneOf("Sophie","Ada","Grace","Barara")
  val maleNameGen   = oneOf("Scott","Fred","Venkat","Daniel")

  def freqSome[T](f1: Int, f2: Int, someGen: Gen[T]): Gen[JOpt[T]] = {
    frequency((f1, someGen.map(JOpt.some(_))), (f2, value(JOpt.none[T])))
  }

  val addressGen = for {
    addr1 <- alphaStr
    addr2 <- freqSome(1, 1, alphaStr)
    city  <- oneOf("Denver","Boulder")
    state <- value("Colorado")
    zip   <- containerOfN[List, Int](5, choose(0, 9)).map(_.mkString)
  } yield new Address(addr1, addr2, city, state, zip)

  type PersonBuilder[T <: Person] = (JOpt[Man], JOpt[Woman], JOpt[Person], JOpt[Address]) => T

  val maleBuilderGen   = for (name <- maleNameGen)   yield new Man(name, _: JOpt[Man], _: JOpt[Woman], _: JOpt[Person], _: JOpt[Address])
  val femaleBuilderGen = for (name <- femaleNameGen) yield new Woman(name, _: JOpt[Man], _: JOpt[Woman], _: JOpt[Person], _: JOpt[Address])
    
  def personGen[T <: Person](genderGen: Gen[PersonBuilder[T]] = oneOf(maleBuilderGen, femaleBuilderGen)): Gen[T] = for {
    builder <- genderGen
    father     <- freqSome(1, 3, personGen(maleBuilderGen))
    mother     <- freqSome(1, 3, personGen(femaleBuilderGen))
    bestFriend <- freqSome(1, 3, personGen())
    address    <- freqSome(9, 1, addressGen)
  } yield builder(father, mother, bestFriend, address)

  implicit val arbPerson = Arbitrary(personGen())
}

object PeopleProps extends Properties("Person") with PeopleGen {
  import Person._
  property("tautology") = forAll((p: Person) => p.father.flatMap(motherF).flatMap(bestFriendF).getOrElse(p) == p || p.father.flatMap(motherF).flatMap(bestFriendF).getOrElse(p) != p)
}

trait OptGen {
  def maybeSome[T](v: T) = oneOf(JOpt.some(v), JOpt.none[T])
  implicit def arbJOpt[T](implicit arb: Arbitrary[T]): Arbitrary[JOpt[T]] = Arbitrary(arbitrary[T].flatMap(maybeSome))
}

object JOptSpec extends Properties("JOpt") with OptGen {
  property("non-emptiness") = forAll((v: JOpt[Int]) => v.isInstanceOf[JOpt.Some[_]] ==> !v.isEmpty) 
  property("emptiness")     = forAll((v: JOpt[Int]) => v.isInstanceOf[JOpt.None[_]] ==> v.isEmpty )

  property("iterability") = forAll(
    (i: Int) => i > Integer.MIN_VALUE ==> {
      var extracted = Integer.MIN_VALUE
      for (ii <- JOpt.some(i)) extracted = ii
      extracted == i
    }
  ) 

  val addOneAndMakeString = new F[Int, String] {
    override def apply(i: Int) = (i + 1).toString
  }

  property("mapSome") = forAll(
    (i: Int) => JOpt.some(i).map(addOneAndMakeString) == JOpt.some((i + 1).toString)
  )

  property("mapNone") = forAll(
    (v: JOpt[Int]) => v.isEmpty ==> (v.map(addOneAndMakeString) == JOpt.none[String])
  )
}

//object PersonSpec extends Specification with ScalaCheck with PeopleGen {
//  val people = containerOfN[List, Person](1000, personGen()).sample.get
//
//  "out of 1000 people we " should {
//    "be able to find out the second line of the addresses of a few's maternal grandfathers" in {
//      val results = for {
//        person <- people
//        mother <- person.mother
//        grandfather <- mother.father
//        address <- grandfather.address
//        line2 <- address.addr2
//      } yield (person.name, mother.name, grandfather.name, line2)
//
//      println(results.mkString("\n"))
//      results must not be empty
//    }
//  }
//}
//
//
//// vim: set ts=4 sw=4 et:
