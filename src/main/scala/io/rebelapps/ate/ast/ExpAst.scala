package io.rebelapps.ate.ast

sealed trait ExpAst

case class CmdAst(name: String, args: List[ArgumentAst]) extends ExpAst

object CmdAst {

  def apply(name: String, args: ArgumentAst*): CmdAst = CmdAst(name, List(args: _*))

}

case class AndCombinator(exp1: ExpAst, exp2: ExpAst)  extends ExpAst
case class OrCombinator(exp1: ExpAst, exp2: ExpAst)   extends ExpAst
case class PipeCombinator(exp1: ExpAst, exp2: ExpAst) extends ExpAst
case class ThenCombinator(exp1: ExpAst, exp2: ExpAst) extends ExpAst
