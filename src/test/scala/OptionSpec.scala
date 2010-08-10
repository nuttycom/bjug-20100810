import org.specs.Specification
import org.specs.ScalaCheck
import org.scalacheck._
import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.scalacheck.Arbitrary.arbitrary
import scala.collection.JavaConversions._

trait PeopleGen {
}

object PersonProps extends Properties("Person") with PeopleGen {
}

object JOptSpec extends Specification {
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


// vim: set ts=4 sw=4 et:
