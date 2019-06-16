package io.rebelapps.ate

import cats.data.{EitherK, State}
import cats.mtl.implicits._
import io.rebelapps.ate.interpreter.argument.Argument
import io.rebelapps.ate.interpreter.argument.Argument.arg
import io.rebelapps.ate.interpreter.cmd.DirCmd.ls
import io.rebelapps.ate.interpreter.cmd.{CatCmd, DirCmd}
import io.rebelapps.ate.interpreter.{Eval, RunTime}
import io.rebelapps.ate.parsing.{Cmd, CmdParser, ShellExpr, SimpleArgumentAst}
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.syntax._

import scala.io.StdIn

object AteApp extends App {

  type T[A] = EitherK[CatCmd, EitherK[Argument, DirCmd, ?], A]

  println("Starting...")

  def Y[A, B](f: (A => B) => (A => B)): A => B = {
    case class W(wf: W => A => B) {
      def apply(w: W) = wf(w)
    }
    val g: W => A => B = w => f(w(w))(_)
    g(W(g))
  }

  def Pcombinator[A, B](fs: Seq[(A => B) => (A =|> B)]): A => B = {
    case class W(wf: W => A => B) {
      def apply(w: W) = wf(w)
    }
    val g: W => A => B = w => fs.tail.foldLeft(fs.head(w(w))) { case (f1, f2) => f1 orElse f2(w(w)) }.apply(_) //f(w(w))(_)
    g(W(g))
  }

  def lsMapper(f: ShellExpr => Fix[T]): ShellExpr =|> Fix[T] = {
    case Cmd("ls", args) => ls[T](args.map(f))
  }

  def argMapper(f: ShellExpr => Fix[T]): ShellExpr =|> Fix[T] = {
    case SimpleArgumentAst(value) => arg[T](value)
  }

  val mapper = Pcombinator(Seq(lsMapper(_), argMapper(_)))


  //  val eval = Interpreter.eval(ls[T](List(arg[T]("dir1"), arg[T]("dir2"))))
  val expr: Fix[T] = ls[T](List(arg[T]("dir1"), arg[T]("dir2")))

  val eval = Eval.eval[T, State[RunTime, ?]](expr)

  val (runtime, result) = eval.run(RunTime()).value

  println(result)

  while (true) {
    val line = StdIn.readLine()
    CmdParser.parse(line) match {
      case Left(err) => println(err)
      case Right(shelExpr) =>
        val fix = mapper.apply(shelExpr)
        val eval = Eval.eval[T, State[RunTime, ?]](fix)

        val (runtime, result) = eval.run(RunTime()).value

        println(result)
    }

  }

  //todo 1) pipe combinator ls file | cat
  //todo 4) foldMap AST
  //todo 5) vfs
}
