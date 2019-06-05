package io.rebelapps.ate.interpreter.cmd

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter
import io.rebelapps.ate.interpreter.{EvalResult, Interpreter, RunTime, ShellExpr}

sealed trait DirCmd[F]

case class LsCmd[F](args: List[F]) extends DirCmd[F]

object DirCmd {

  implicit val dirInterpreter = new Interpreter[DirCmd] {

    override def eval[G[_]](expr: DirCmd[ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, EvalResult] = {
      expr match {
        case LsCmd(args) =>
          for {
            results <- args.traverse(Interpreter.eval(_))
            arguments = results.map(_.outputStr)
          } yield EvalResult().putLn(arguments.mkString(","))
      }
    }

  }

}
