package io.rebelapps.ate.interpreter.cmd

import io.rebelapps.ate.interpreter.ShellExpr
import io.rebelapps.ate.util.syntax.:<:

sealed trait DirCmd[F]

case class LsCmd[F](args: List[F]) extends DirCmd[F]

object DirCmd {

  def ls[F[_]](args: List[ShellExpr[F]])(implicit ev: DirCmd :<: F): ShellExpr[F] = ShellExpr.inject(LsCmd(args): DirCmd[ShellExpr[F]])

  implicit val dirInterpreter = DirHoneypotInterpreter

}
