public class Address {
  public final String addr1;
  public final JOpt<String> addr2;
  public final String city;
  public final String state;
  public final String zip;

  public Address(String addr1, JOpt<String> addr2, String city, String state, String zip) {
    this.addr1 = addr1;
    this.addr2 = addr2;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }
}
