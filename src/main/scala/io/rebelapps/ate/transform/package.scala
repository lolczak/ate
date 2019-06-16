package io.rebelapps.ate

import io.rebelapps.ate.util.syntax.=|>

package object transform {

  def Ytransform[A, B](fs: Seq[(A => B) => (A =|> B)]): A => B = {
    case class W(wf: W => A => B) {
      def apply(w: W) = wf(w)
    }
    val g: W => A => B = w => fs.tail.foldLeft(fs.head(w(w))) { case (f1, f2) => f1 orElse f2(w(w)) }.apply(_) //f(w(w))(_)
    g(W(g))
  }

}
