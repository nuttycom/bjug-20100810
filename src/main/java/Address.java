public class Address {
  public final String addr1;
  public final String addr2;
  public final String city;
  public final String state;
  public final String zip;

  /**
   * @param addr2 may be null
   */
  public Address(String addr1, String addr2, String city, String state, String zip) {
    this.addr1 = addr1;
    this.addr2 = addr2;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }
}
