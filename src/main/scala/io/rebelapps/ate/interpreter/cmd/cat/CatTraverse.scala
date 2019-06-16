package io.rebelapps.ate.interpreter.cmd.cat

import cats.implicits._
import cats.{Applicative, Traverse}

object CatTraverse extends Traverse[CatCmd] {

  override def traverse[G[_], A, B](fa: CatCmd[A])(f: A => G[B])(implicit evidence$1: Applicative[G]): G[CatCmd[B]] = {
    fa.args
      .traverse(f)
      .map(CatCmd.apply)
  }

  override def foldLeft[A, B](fa: CatCmd[A], b: B)(f: (B, A) => B): B = ???

  override def foldRight[A, B](fa: CatCmd[A], lb: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] = ???

}
