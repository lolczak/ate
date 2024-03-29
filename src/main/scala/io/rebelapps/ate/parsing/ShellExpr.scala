package io.rebelapps.ate.parsing

sealed trait ShellExpr

case class Cmd(name: String, args: List[ArgumentAst]) extends ShellExpr

object Cmd {

  def apply(name: String, args: ArgumentAst*): Cmd = Cmd(name, List(args: _*))

}

case class AndCombinator(exp1: ShellExpr, exp2: ShellExpr)  extends ShellExpr
case class OrCombinator(exp1: ShellExpr, exp2: ShellExpr)   extends ShellExpr
case class PipeCombinator(exp1: ShellExpr, exp2: ShellExpr) extends ShellExpr
case class ThenCombinator(exp1: ShellExpr, exp2: ShellExpr) extends ShellExpr

sealed trait ArgumentAst extends ShellExpr
case class SimpleArgumentAst(value: String)  extends ArgumentAst
case class CmdSubstitution(cmd: ShellExpr) extends ArgumentAst
case class SingleQuoted(value: String)    extends ArgumentAst
case class DoubleQuoted(value: String)    extends ArgumentAst