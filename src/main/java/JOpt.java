import java.util.Iterator;

public abstract class JOpt<A> implements Iterable<A> {

  public A getOrElse(A dflt) {
    for (A a : this) return a;
    return dflt;
  }

  public <B> JOpt<B> map(F<A, B> f) {
    for (A a : this) return JOpt.some(f.apply(a));
    return JOpt.<B>none();
  }

  public <B> JOpt<B> flatMap(F<A, JOpt<B>> f) {
    for (A a : this) return f.apply(a);
    return JOpt.<B>none();
  }

  public static <A> JOpt<A> some(A a) {
    return new Some<A>(a);
  }

  public static <A> JOpt<A> none() {
    return new None<A>();
  }

  public static class Some<A> extends JOpt<A> {
    private final A value;
    public Some(A a) {
      this.value = a;
    }

    public Iterator<A> iterator() {
      return new Iterator<A>() {
        boolean hasNext = true;

        public boolean hasNext() {
          return hasNext;
        }

        public A next() {
          if (hasNext) {
            hasNext = false;
            return value;
          } else {
            throw null;
          }
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }

  public static class None<A> extends JOpt<A> {
    public Iterator<A> iterator() {
      return new Iterator<A>() {
        public boolean hasNext() {
          return false;
        }

        public A next() {
          throw null;
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }
  }
}

