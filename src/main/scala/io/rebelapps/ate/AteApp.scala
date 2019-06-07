package io.rebelapps.ate

import cats.data.EitherK
import io.rebelapps.ate.interpreter.argument.Argument
import io.rebelapps.ate.interpreter.argument.Argument.arg
import io.rebelapps.ate.interpreter.cmd.DirCmd
import io.rebelapps.ate.interpreter.cmd.DirCmd.ls
import io.rebelapps.ate.interpreter.{Eval, Interpreter, RunTime}

object AteApp extends App {

  type T[A] = EitherK[Argument, DirCmd, A]

  println("Starting...")

//  val eval = Interpreter.eval(ls[T](List(arg[T]("dir1"), arg[T]("dir2"))))
  val eval = Eval.eval(ls[T](List(arg[T]("dir1"), arg[T]("dir2"))))

  val (runtime, result) = eval.run(RunTime()).value

  println(result)

  //todo 1) pipe combinator cat file | grep dupa
  //todo 3) tagless final or hkp
  //todo 4) foldMap AST
  //todo 5) vfs
}
