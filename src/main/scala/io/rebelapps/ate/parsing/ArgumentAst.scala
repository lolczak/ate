package io.rebelapps.ate.parsing

sealed trait ArgumentAst extends ShellExpr
case class SimpleArgumentAst(value: String)  extends ArgumentAst
case class CmdSubstitution(cmd: ShellExpr) extends ArgumentAst
case class SingleQuoted(value: String)    extends ArgumentAst
case class DoubleQuoted(value: String)    extends ArgumentAst
