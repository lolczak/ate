package io.rebelapps.ate.interpreter.argument

import cats.{Applicative, Monad, Traverse}
import cats.mtl.MonadState
import io.rebelapps.ate.interpreter.{Eval, EvalResult, Result, RunTime}
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.:<:

case class Argument[F](value: String)

object Argument {

  def arg[F[_]](value: String)(implicit ev: Argument :<: F): Fix[F] = Fix.inject(Argument[Fix[F]](value))

  implicit val argInterpreter = ArgHoneypotInterpreter

  implicit val argEval = new Eval[Argument] {
    override def eval[G[_]](exp: Argument[Result])
                           (implicit F: Traverse[Argument], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result] =
      G1.point(EvalResult(List(exp.value)))
  }

  implicit val argTraverse = new Traverse[Argument] {
    override def traverse[G[_], A, B](fa: Argument[A])(f: A => G[B])(implicit G: Applicative[G]): G[Argument[B]] = {
      G.point(fa.asInstanceOf[Argument[B]])
    }

    override def foldLeft[A, B](fa: Argument[A], b: B)(f: (B, A) => B): B = ???

    override def foldRight[A, B](fa: Argument[A], lb: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] = ???
  }

}
