sealed trait SOpt[+A] {
  def cata[X](ifSome: A => X, ifNone: => X): X
 
  def map[B](f: A => B): SOpt[B] = error("todo")
 
  def flatMap[B](f: A => SOpt[B]): SOpt[B] = error("todo")
 
  def getOrElse[AA >: A](e: => AA): AA = error("todo")
 
  def filter(p: A => Boolean): SOpt[A] = error("todo")
 
  def foreach(f: A => Unit): Unit = error("todo")
 
  def isDefined: Boolean = error("todo")
 
  def isEmpty: Boolean = error("todo")
 
  def get: A = error("todo")
 
  def orElse[AA >: A](o: SOpt[AA]): SOpt[AA] = error("todo")
 
  def toLeft[X](right: => X): Either[A, X] = error("todo")
 
  def toRight[X](left: => X): Either[X, A] = error("todo")
 
  def toList: List[A] = error("todo")
 
  def iterator: Iterator[A] = error("todo")
}

object SOpt {
  def some[A](a: A) = new SOpt[A] {
    override def cata[X](ifSome: A => X, ifNone: => X): X = ifSome(a)
  }
  
  def none[A] = new SOpt[A] {
    override def cata[X](ifSome: A => X, ifNone: => X): X = ifNone
  }
}

