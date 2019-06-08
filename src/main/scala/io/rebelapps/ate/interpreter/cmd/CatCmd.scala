package io.rebelapps.ate.interpreter.cmd

import cats.implicits._
import cats.mtl.MonadState
import cats.{Applicative, Monad, Traverse}
import io.rebelapps.ate.interpreter._
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.:<:

case class CatCmd[F](args: List[F])

object CatCmd {

  def cat[F[_]](args: List[Fix[F]])(implicit ev: CatCmd :<: F): Fix[F] = Fix.inject(CatCmd(args))

  implicit val catEval = new Eval[CatCmd] {

    override def eval[G[_]](exp: CatCmd[Result])
                           (implicit F: Traverse[CatCmd], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result] = {
      import G0._, G1._
      if (exp.args.nonEmpty) {
        val x = exp.args.flatMap(_.fold(_.results)(run => List(run.outputStr)))

        point(RunResult().putLn(x.mkString(" "))) //todo stdout change
      } else {
        inspect(_.stdIn) >>= (in => point(RunResult(in.toVector.map(StdOut(_)))) )
      }
    }

  }

  implicit val catTraverse = new Traverse[CatCmd] {

    override def traverse[G[_], A, B](fa: CatCmd[A])(f: A => G[B])(implicit evidence$1: Applicative[G]): G[CatCmd[B]] = {
      fa.args
        .traverse(f)
        .map(CatCmd.apply)
    }

    override def foldLeft[A, B](fa: CatCmd[A], b: B)(f: (B, A) => B): B = ???

    override def foldRight[A, B](fa: CatCmd[A], lb: cats.Eval[B])(f: (A, cats.Eval[B]) => cats.Eval[B]): cats.Eval[B] = ???

  }
}