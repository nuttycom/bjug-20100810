import org.specs.Specification
import org.specs.ScalaCheck
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary.arbitrary
import scala.collection.JavaConversions._

trait PeopleGen {
  val maleNameGen = oneOf("Fred","Venkat","Scott")
  val femaleNameGen = oneOf("Sophie","Ada","Barbara")

  val nameGen = oneOf(maleNameGen, femaleNameGen)

  def freqSome[T](f1: Int, f2: Int, gen: Gen[T])= frequency(
    (f1, gen.map((t: T) => Some(t))), (f2, value(None)) 
  )

  def personGen(nameGen: Gen[String]): Gen[Person] = for {
    name <- nameGen
    father <- freqSome(1, 3, personGen(maleNameGen))
    mother <- freqSome(1, 3, personGen(femaleNameGen))
  } yield new Person(name, father, mother)

  implicit val arbPerson = Arbitrary(personGen(nameGen))
}

object PersonProps extends Properties("Person") with PeopleGen {
  import Person._
  property("tautology") = forAll {
    (p: Person) => try {
      p.mother.flatMap(m => m.father).map(p => p.name == "none" || p.name != "none").getOrElse(true)
    } catch {
      case ex => false
    }
  }

  property("comprehensionTautology") = forAll {
    (p: Person) => try {
      val truth = for (mother <- p.mother; grandfather <- mother.father) 
                  yield grandfather.name == "Fred" || grandfather.name != "Fred"

      truth.getOrElse(true)
    } catch {
      case ex => false
    }
  }
}

object PersonSpec extends Specification with ScalaCheck with PeopleGen {
  val people = containerOfN[List, Person](1000, personGen(nameGen)).sample.get

  "out of 1000 people we" should {
    "be able to find out the names of a few's maternal grandfathers" in {
      val results = for {
        person <- people
        mother <- person.mother
        grandfather <- mother.father
      } yield (person.name, mother.name, grandfather.name)

      println(results.mkString("\n"))
      results must not be empty
    }
  }
}
