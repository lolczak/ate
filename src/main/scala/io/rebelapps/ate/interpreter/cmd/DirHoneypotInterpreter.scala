package io.rebelapps.ate.interpreter.cmd

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, RunTime}
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax.\/

object DirHoneypotInterpreter extends Interpreter[DirCmd] {

  override def eval[G[_]](expr: DirCmd[Fix[G]])(implicit G: Interpreter[G]): State[RunTime, List[String] \/ EvalResult] = {
    expr match {
      case LsCmd(args) =>
        for {
          results <- args.traverse(Interpreter.eval(_))
          arguments = results.flatMap {
            case Left(list) => list
            case Right(evalR) => evalR.outputStr
          }
        } yield EvalResult().putLn(arguments.mkString(",")).asRight
    }
  }

}
