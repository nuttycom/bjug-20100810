import scala.Option;

public class Person {
  public final String name;
  public final Option<Person> father;
  public final Option<Person> mother;
  public final Option<Address> address;

  public Person(String name, Option<Person> father, Option<Person> mother, Option<Address> address) {
    this.name = name;
    this.father = father;
    this.mother = mother;
    this.address = address;
  }
}
