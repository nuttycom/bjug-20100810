public class Person {
  public final String name;
  public final Person father;
  public final Person mother;
  public final Address address;

  /**
   * @param name the person's name - you have to have this!
   * @param father may be null
   * @param mother may be null
   * @param address may be null
   */
  public Person(String name, Person father, Person mother, Address address) {
    this.name = name;
    this.father = father;
    this.mother = mother;
    this.address = address;
  }
}
