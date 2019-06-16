package io.rebelapps.ate.interpreter.cmd.cat

import cats.implicits._
import cats.mtl.MonadState
import cats.{Monad, Traverse}
import io.rebelapps.ate.interpreter._

object CatHoneypotInterpreter extends Eval[CatCmd] {

  override def eval[G[_]](exp: CatCmd[Result])
                         (implicit F: Traverse[CatCmd], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result] = {
    import G0._
    import G1._
    if (exp.args.nonEmpty) {
      val x = exp.args.flatMap(_.fold(_.results)(run => List(run.outputStr)))

      point(RunResult().putLn(x.mkString(" "))) //todo stdout change
    } else {
      inspect(_.stdIn) >>= (in => point(RunResult(in.toVector.map(StdOut(_)))))
    }
  }

}
