import scala.Option;

public class Person {
  public final String name;
  public final Option<Person> father;
  public final Option<Person> mother;

  /**
   * @param name the person's name - you have to have this!
   * @param father may be null
   * @param mother may be null
   * @param address may be null
   */
  public Person(String name, Option<Person> father, Option<Person> mother) {
    this.name = name;
    this.father = father;
    this.mother = mother;
  }
}
