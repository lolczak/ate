package io.rebelapps.ate.interpreter.cmd.dir

import cats.implicits._
import cats.{Applicative, Traverse}

object DirTraverse extends Traverse[DirCmd] {

  override def traverse[G[_], A, B](fa: DirCmd[A])(f: A => G[B])(implicit evidence$1: Applicative[G]): G[DirCmd[B]] = {
    fa match {
      case LsCmd(args) =>
        args
          .traverse(f)
          .map(LsCmd.apply)
    }
  }

  override def foldLeft[A, B](fa: DirCmd[A], b: B)(f: (B, A) => B): B = ???

  override def foldRight[A, B](fa: DirCmd[A], lb: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] = ???

}
