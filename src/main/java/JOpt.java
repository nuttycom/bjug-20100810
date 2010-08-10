import java.util.Iterator;

public abstract class JOpt<A> implements Iterable<A> {

  public abstract boolean isEmpty();
  public abstract Iterator<A> iterator();

  public JOpt<A> filter(F<A, Boolean> f) {
    for (A a : this) return (f.apply(a) ? this : JOpt.<A>none());
    return this;
  }

  public <B> JOpt<B> map(F<A, B> f) {
    for (A a : this) return JOpt.some(f.apply(a));
    return JOpt.<B>none();
  }
  
  public <B> JOpt<B> flatMap(F<A, JOpt<B>> f) {
    for (A a : this) return f.apply(a);
    return JOpt.<B>none();
  }

  public A getOrElse(A a) {
    for (A aa : this) return aa;
    return a;
  }

  public <B> B fold(B b, F2<A, B, B> f) {
    for (A a : this) return f.apply(a, b);
    return b;
  }

  public <B> F<B, B> fold(final F<A, B> f, final F2<B, B, B> append) {
    return this.<F<B, B>>fold(
      new F<A, F<B, B>>() {
        public F<B, B> apply(final A a) {
          return new F<B, B>() {
            public B apply(final B b) {
              return append.apply(f.apply(a), b);
            }
          };
        }
      },
      new F<B, B>() {
        public B apply(B b) {
          return b;
        }
      }
    );
//    for (final A a : this) return new F<B, B>() {
//      public B apply(B b) {
//        return append.apply(f.apply(a), b);
//      }
//    };
//
//    return new F<B, B>() {
//      public B apply(B b) {
//        return b;
//      }
//    };
  }

  public <B> B fold(final F<A, B> f, final B b) {
    for (final A a : this) return f.apply(a);
    return b;
  }

  public static <A> JOpt<A> none() {
    return new None<A>();
  }

  public static <A> JOpt<A> some(A value) {
    return new Some<A>(value);
  }

  public static <A> JOpt<A> option(A value) {
    return (value == null ? JOpt.<A>none() : some(value));
  }

  public static class None<A> extends JOpt<A> {
    @Override public boolean isEmpty() {
      return true;
    }

    @Override public Iterator<A> iterator() {
      return new Iterator<A>() {
        public boolean hasNext() {
          return false;
        }

        public A next() {
          throw new IllegalStateException("Serious programmer error.");
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override public boolean equals(Object other) {
      return other instanceof None;
    }
  }

  public static class Some<A> extends JOpt<A> {
    private final A value;
    public Some(A value) {
      this.value = value;
    }

    @Override public boolean isEmpty() {
      return false;
    }

    @Override public Iterator<A> iterator() {
      return new Iterator<A>() {
        boolean hasNext = true;
        public boolean hasNext() {
          return hasNext;
        }

        public A next() {
          if (!hasNext) throw new IllegalStateException("Serious programmer error.");
          hasNext = false;
          return value;
        }

        public void remove() {
          throw new UnsupportedOperationException();
        }
      };
    }

    @Override public boolean equals(Object other) {
      return other instanceof Some && ((Some) other).value.equals(value);
    }
  }

}

