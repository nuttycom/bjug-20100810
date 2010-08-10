public class Person {
  public final String name;
  public final JOpt<Man> father;
  public final JOpt<Woman> mother;
  public final JOpt<Person> bestFriend;
  public final JOpt<Address> address;

  public Person(String name, JOpt<Man> father, JOpt<Woman> mother, JOpt<Person> bestFriend, JOpt<Address> address) {
    this.name = name;
    this.father = father;
    this.mother = mother;
    this.bestFriend = bestFriend;
    this.address = address;
  }

  public static F<Person, JOpt<Man>> fatherF = new F<Person, JOpt<Man>>() {
    public JOpt<Man> apply(Person p) {
      return p.father;
    }
  };

  public static F<Person, JOpt<Woman>> motherF = new F<Person, JOpt<Woman>>() {
    public JOpt<Woman> apply(Person p) {
      return p.mother;
    }
  };

  public static F<Person, JOpt<Person>> bestFriendF = new F<Person, JOpt<Person>>() {
    public JOpt<Person> apply(Person p) {
      return p.bestFriend;
    }
  };
}
