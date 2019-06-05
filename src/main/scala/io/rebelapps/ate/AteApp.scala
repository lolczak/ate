package io.rebelapps.ate

import cats.data.EitherK
import io.rebelapps.ate.interpreter.argument.Argument
import io.rebelapps.ate.interpreter.cmd.{DirCmd, LsCmd}
import io.rebelapps.ate.interpreter.{Interpreter, RunTime, ShellExpr}
import io.rebelapps.ate.util.syntax.:<:

object AteApp extends App {

  type T[A] = EitherK[Argument, DirCmd, A]

  def ls[F[_]](args: List[ShellExpr[F]])(implicit ev: DirCmd :<: F): ShellExpr[F] = ShellExpr.inject(LsCmd(args): DirCmd[ShellExpr[F]])

  def str[F[_]](value: String)(implicit ev: Argument :<: F): ShellExpr[F] = ShellExpr.inject(Argument[ShellExpr[F]](value))

  println("Starting...")

  val eval = Interpreter.eval(ls[T](List(str[T]("dir1"), str[T]("dir2"))))

  val (runtime, result) = eval.run(RunTime()).value

  println(result.outputStr)

}
