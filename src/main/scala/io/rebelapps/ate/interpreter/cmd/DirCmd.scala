package io.rebelapps.ate.interpreter.cmd

import cats.{Applicative, Traverse}
import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter.{Eval, Result, RunResult, RunTime}
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.:<:

sealed trait DirCmd[F]

case class LsCmd[F](args: List[F]) extends DirCmd[F]

object DirCmd {

  def ls[F[_]](args: List[Fix[F]])(implicit ev: DirCmd :<: F): Fix[F] = Fix.inject(LsCmd(args): DirCmd[Fix[F]])

  implicit val dirInterpreter = DirHoneypotInterpreter

  implicit val dirEval = new Eval[DirCmd] {
    override def eval(exp: DirCmd[Result])(implicit F: Traverse[DirCmd]): State[RunTime, Result] = {
      exp match {
        case LsCmd(args) =>
          val x = args.flatMap(_.fold(_.results)(run => List(run.outputStr)))
          State.pure(RunResult().putLn(x.mkString(" "))) //todo stdout change
      }
    }
  }

  implicit val dirTraverse = new Traverse[DirCmd] {
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

}
