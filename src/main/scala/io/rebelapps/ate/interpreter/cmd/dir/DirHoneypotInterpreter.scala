package io.rebelapps.ate.interpreter.cmd.dir

import cats.mtl.MonadState
import cats.{Monad, Traverse}
import io.rebelapps.ate.interpreter.{Eval, Result, RunResult, RunTime}

object DirHoneypotInterpreter extends Eval[DirCmd] {

  override def eval[G[_]](exp: DirCmd[Result])(implicit F: Traverse[DirCmd], G0: MonadState[G, RunTime], G1: Monad[G]): G[Result] =
    exp match {
      case LsCmd(args) =>
        val x = args.flatMap(_.fold(_.results)(run => List(run.outputStr)))

        G1.point(RunResult().putLn(x.mkString(" "))) //todo stdout change
    }

}
