package io.rebelapps.ate

import cats.data.{EitherK, State}
import cats.mtl.implicits._
import io.rebelapps.ate.interpreter.argument.Argument
import io.rebelapps.ate.interpreter.argument.Argument.arg
import io.rebelapps.ate.interpreter.cmd.{CatCmd, DirCmd}
import io.rebelapps.ate.interpreter.cmd.DirCmd.ls
import io.rebelapps.ate.interpreter.{Eval, RunTime}

object AteApp extends App {

  type T[A] = EitherK[CatCmd, EitherK[Argument, DirCmd, ?], A]

  println("Starting...")

  //  val eval = Interpreter.eval(ls[T](List(arg[T]("dir1"), arg[T]("dir2"))))
  val eval = Eval.eval[T, State[RunTime, ?]](ls[T](List(arg[T]("dir1"), arg[T]("dir2"))))

  val (runtime, result) = eval.run(RunTime()).value

  println(result)

  //todo 1) pipe combinator ls file | cat
  //todo 4) foldMap AST
  //todo 5) vfs
}
