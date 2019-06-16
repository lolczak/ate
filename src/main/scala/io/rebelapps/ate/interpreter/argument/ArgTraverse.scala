package io.rebelapps.ate.interpreter.argument

import cats.{Applicative, Traverse}

object ArgTraverse extends Traverse[Argument] {

  override def traverse[G[_], A, B](fa: Argument[A])(f: A => G[B])(implicit G: Applicative[G]): G[Argument[B]] = {
    G.point(fa.asInstanceOf[Argument[B]])
  }

  override def foldLeft[A, B](fa: Argument[A], b: B)(f: (B, A) => B): B = ???

  override def foldRight[A, B](fa: Argument[A], lb: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] = ???
  
}
