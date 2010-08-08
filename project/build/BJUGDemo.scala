import sbt._

class BJUGDemo(info: ProjectInfo) extends DefaultProject(info) {
  val specs      = "org.scala-tools.testing" % "specs_2.8.0"      % "1.6.5" % "test"
  val scalacheck = "org.scala-tools.testing" % "scalacheck_2.8.0" % "1.7"   % "test"
}

// vim: set ts=4 sw=4 et:
