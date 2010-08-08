import org.specs.Specification
import org.specs.ScalaCheck
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary.arbitrary
import scala.collection.JavaConversions._

object FConversions {
  implicit def f2scalaFunction[A, B](f: F[A, B]): A => B = (a: A) => f.apply(a)
  implicit def scalaFunction2F[A, B](f: A => B): F[A, B] = new F[A,B] {
    def apply(a: A) = f(a)
  }
}

trait OptGen {
  def maybeSome[T](v: T) = oneOf(JOpt.some(v), JOpt.none[T])
  implicit def arbJOpt[T](implicit arb: Arbitrary[T]): Arbitrary[JOpt[T]] = Arbitrary(arbitrary[T].flatMap(maybeSome))
}

object JOptSpec extends Properties("JOpt") with OptGen {
  import FConversions._

  property("non-emptiness") = forAll((v: JOpt[Int]) => v.isInstanceOf[JOpt.Some[_]] ==> !v.isEmpty) 
  property("emptiness")     = forAll((v: JOpt[Int]) => v.isInstanceOf[JOpt.None[_]] ==> v.isEmpty )

  property("iterability") = forAll(
    (i: Int) => i > Integer.MIN_VALUE ==> {
      var extracted = Integer.MIN_VALUE
      for (ii <- JOpt.some(i)) extracted = ii
      extracted == i
    }
  ) 

  property("mapSome") = forAll(
    (i: Int) => JOpt.some(i).map((x: Int) => (x + 1).toString) == JOpt.some((i + 1).toString)
  )

  property("mapNone") = forAll(
    (v: JOpt[Int]) => v.isEmpty ==> (v.map((x: Int) => (x + 1).toString) == JOpt.none[String])
  )
}

trait PeopleGen {
  val femaleNameGen = oneOf("Sophie","Ada","Grace","Barara")
  val maleNameGen   = oneOf("Scott","Fred","Venkat","Daniel")

  def optGen[T](f1: Int, f2: Int, someGen: Gen[T]): Gen[Option[T]] = {
    frequency((f1, someGen.map(Some(_))), (f2, value(None)))
  }

  val addressGen = for {
    addr1 <- alphaStr
    addr2 <- optGen(1, 1, alphaStr)
    city  <- oneOf("Denver","Boulder")
    state <- value("Colorado")
    zip   <- containerOfN[List, Int](5, choose(0, 9)).map(_.mkString)
  } yield new Address(addr1, addr2, city, state, zip)

  def personGen(nameGen: Gen[String] = frequency((1, maleNameGen), (1, femaleNameGen))): Gen[Person] = for {
    name <- nameGen
    father <- optGen(1, 3, personGen(maleNameGen))
    mother <- optGen(1, 3, personGen(femaleNameGen))
    address <- optGen(9, 1, addressGen)
  } yield new Person(name, father, mother, address)
}

object PersonSpec extends Specification with ScalaCheck with PeopleGen {
  val people = containerOfN[List, Person](1000, personGen()).sample.get

  "out of 1000 people we " should {
    "be able to find out the second line of the addresses of a few's maternal grandfathers" in {
      val results = for {
        person <- people
        mother <- person.mother
        grandfather <- mother.father
        address <- grandfather.address
        line2 <- address.addr2
      } yield (person.name, mother.name, grandfather.name, line2)

      println(results)
      pass
    }
  }
}


// vim: set ts=4 sw=4 et:
