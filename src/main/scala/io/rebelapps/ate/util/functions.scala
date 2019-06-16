package io.rebelapps.ate.util

object functions {

  def Y[A, B](f: (A => B) => (A => B)): A => B = {
    case class Aux(wf: Aux => A => B) {
      def apply(w: Aux) = wf(w)
    }
    val g: Aux => A => B = w => f(w(w))(_)
    g(Aux(g))
  }

}
