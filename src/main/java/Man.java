class Man extends Person {
  public Man(String name, JOpt<Man> father, JOpt<Woman> mother, JOpt<Person> bestFriend, JOpt<Address> address) {
    super(name, father, mother, bestFriend, address);
  }    
}
