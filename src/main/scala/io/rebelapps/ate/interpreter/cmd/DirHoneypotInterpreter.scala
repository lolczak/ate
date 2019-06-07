package io.rebelapps.ate.interpreter.cmd

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter._
import io.rebelapps.ate.util.data.Fix

object DirHoneypotInterpreter extends Interpreter[DirCmd] {

  override def eval[G[_]](expr: DirCmd[Fix[G]])(implicit G: Interpreter[G]): State[RunTime, Result] = {
    expr match {
      case LsCmd(args) =>
        for {
          results   <- args.traverse(Interpreter.eval(_))
          arguments  = results.flatMap { _.fold(_.results)(run => List(run.outputStr)) }
        } yield RunResult().putLn(arguments.mkString(","))
    }
  }

}
