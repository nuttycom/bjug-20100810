sealed trait SOpt[+A] {
  import SOpt._

  def fold[B](ifSome: A => B, ifNone: => B): B
 
  def map[B](f: A => B): SOpt[B] = fold(a => some(f(a)), none[B])
 
  def flatMap[B](f: A => SOpt[B]): SOpt[B] = fold(f, none[B])
 
  def getOrElse[AA >: A](e: => AA): AA = error("todo")
 
  def filter(p: A => Boolean): SOpt[A] = error("todo")
 
  def foreach(f: A => Unit): Unit = error("todo")
 
  def isDefined: Boolean = error("todo")
 
  def isEmpty: Boolean = error("todo")
 
  def get: A = error("todo")
 
  def orElse[AA >: A](o: SOpt[AA]): SOpt[AA] = error("todo")
 
  def toLeft[B](right: => B): Either[A, B] = error("todo")
 
  def toRight[B](left: => B): Either[B, A] = error("todo")
 
  def toList: List[A] = error("todo")
 
  def iterator: Iterator[A] = error("todo")
}

object SOpt {
  def some[A](a: A) = new SOpt[A] {
    override def fold[B](ifSome: A => B, ifNone: => B): B = ifSome(a)
  }
  
  def none[A] = new SOpt[A] {
    override def fold[B](ifSome: A => B, ifNone: => B): B = ifNone
  }
}

