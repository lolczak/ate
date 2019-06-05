package io.rebelapps.ate.interpreter.cmd

import cats.data.State
import cats.implicits._
import io.rebelapps.ate.interpreter
import io.rebelapps.ate.interpreter.{Interpreter, ShellExpr}

sealed trait DirCmd[F]

case class Ls[F](args: List[F]) extends DirCmd[F]

object DirCmd {

  implicit val dirInterpreter = new Interpreter[DirCmd] {

    override def eval[G[_]](expr: DirCmd[ShellExpr[G]])(implicit G: Interpreter[G]): State[interpreter.RunTime, String] = {
      expr match {
        case Ls(args) =>
          for {
            arguments <- args.traverse(Interpreter.eval(_))
          } yield arguments.mkString(",")
      }
    }

  }

}
