import org.specs.Specification
import org.specs.ScalaCheck
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary.arbitrary
import scala.collection.JavaConversions._

trait PeopleGen {
  val femaleNameGen = oneOf("Sophie","Ada","Grace","Barbara")
  val maleNameGen   = oneOf("Scott","Fred","Venkat","Daniel")


  def maybeGen[T](f1: Int, f2: Int, someGen: Gen[T]): Gen[T] = {
    frequency((f1, someGen), (f2, value[T](null.asInstanceOf[T])))
  }

  val addressGen = for {
    addr1 <- alphaStr
    addr2 <- maybeGen(1, 1, alphaStr)
    city  <- oneOf("Denver","Boulder")
    state <- value("Colorado")
    zip   <- containerOfN[List, Int](5, choose(0, 9)).map(_.mkString)
  } yield new Address(addr1, addr2, city, state, zip)

  def personGen(nameGen: Gen[String] = frequency((1, maleNameGen), (1, femaleNameGen))): Gen[Person] = for {
    name    <- nameGen
    father  <- maybeGen(1, 3, personGen(maleNameGen))
    mother  <- maybeGen(1, 3, personGen(femaleNameGen))
    address <- maybeGen(9, 1, addressGen)
  } yield new Person(name, father, mother, address)

  implicit val arbPerson = Arbitrary(personGen())
}

object PersonProps extends Properties("Person") with PeopleGen {
  property("tautology") = forAll((p: Person) => p.mother.father.address.addr2 == "123 45th St." || p.mother.father.address.addr2 != "123 45th St.")
}

//object JOptSpec extends Properties("JOpt") with OptGen {
//  property("non-emptiness") = forAll((v: JOpt[Any]) => v.isInstanceOf[JOpt.Some[_]] ==> !v.isEmpty) 
//  property("emptiness")     = forAll((v: JOpt[Any]) => v.isInstanceOf[JOpt.None[_]] ==> v.isEmpty )
//
//  property("iterability") = forAll(
//    (i: Int) => i > Integer.MIN_VALUE ==> {
//      var extracted = Integer.MIN_VALUE
//      for (ii <- JOpt.some(i)) extracted = ii
//      extracted == i
//    }
//  ) 
//}

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


// vim: set ts=4 sw=4 et:
