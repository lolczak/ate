package io.rebelapps.ate.ast

sealed trait ShellExp

case class Cmd(name: String, args: List[Argument]) extends ShellExp

object Cmd {

  def apply(name: String, args: Argument*): Cmd = Cmd(name, List(args: _*))

}

case class AndCombinator(exp1: ShellExp, exp2: ShellExp)  extends ShellExp
case class OrCombinator(exp1: ShellExp, exp2: ShellExp)   extends ShellExp
case class PipeCombinator(exp1: ShellExp, exp2: ShellExp) extends ShellExp
case class ThenCombinator(exp1: ShellExp, exp2: ShellExp) extends ShellExp
