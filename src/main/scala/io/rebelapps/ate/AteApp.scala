package io.rebelapps.ate

import cats.arrow.FunctionK
import cats.data.{EitherK, State}
import io.rebelapps.ate.interpreter.argument.Argument
import io.rebelapps.ate.interpreter.cmd.{DirCmd, Ls}
import io.rebelapps.ate.interpreter.{Interpreter, RunTime, ShellExpr}
import io.rebelapps.ate.util.syntax.:<:

object AteApp extends App {

  type T[A] = EitherK[Argument, DirCmd, A]

  def ls[F[_]](args: List[ShellExpr[F]])(implicit ev: DirCmd :<: F): ShellExpr[F] = ShellExpr.inject(Ls(args): DirCmd[ShellExpr[F]])

  def str[F[_]](value: String)(implicit ev: Argument :<: F): ShellExpr[F] = ShellExpr.inject(Argument[ShellExpr[F]](value))

  implicit def coInterpreter[F[_], H[_]](implicit F0: Interpreter[F], H0: Interpreter[H]) = new Interpreter[EitherK[F, H, ?]] {
    override def eval[G[_]](expr: EitherK[F, H, ShellExpr[G]])(implicit G: Interpreter[G]): State[RunTime, String] = {
      expr.run.fold(F0.eval(_), H0.eval(_))
    }
  }

  println("Starting...")

  val eval = Interpreter.eval(ls[T](List(str[T]("test"))))

  println(eval.run(RunTime()).value)

}
