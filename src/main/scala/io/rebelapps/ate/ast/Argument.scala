package io.rebelapps.ate.ast

sealed trait Argument
case class SimpleArgument(value: String)  extends Argument
case class CmdSubstitution(cmd: ExpAst) extends Argument
case class SingleQuoted(value: String)    extends Argument
case class DoubleQuoted(value: String)    extends Argument
