package io.rebelapps.ate.ast

sealed trait ExpAst

case class Cmd(name: String, args: List[Argument]) extends ExpAst

object Cmd {

  def apply(name: String, args: Argument*): Cmd = Cmd(name, List(args: _*))

}

case class AndCombinator(exp1: ExpAst, exp2: ExpAst)  extends ExpAst
case class OrCombinator(exp1: ExpAst, exp2: ExpAst)   extends ExpAst
case class PipeCombinator(exp1: ExpAst, exp2: ExpAst) extends ExpAst
case class ThenCombinator(exp1: ExpAst, exp2: ExpAst) extends ExpAst
