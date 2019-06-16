package io.rebelapps.ate

import cats.data.{EitherK, State}
import cats.mtl.implicits._
import io.rebelapps.ate.interpreter.argument.{ArgTransformer, Argument}
import io.rebelapps.ate.interpreter.argument.Argument.arg
import io.rebelapps.ate.interpreter.cmd.cat.{CatCmd, CatTransformer}
import io.rebelapps.ate.interpreter.cmd.dir.{DirCmd, DirTransformer}
import io.rebelapps.ate.interpreter.cmd.dir.DirCmd.ls
import io.rebelapps.ate.interpreter.{Eval, HoneypotInstances, RunTime}
import io.rebelapps.ate.parsing.{Cmd, CmdParser, ShellExpr, SimpleArgumentAst}
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.transform.Ytransform

import scala.io.StdIn

object AteApp extends App with HoneypotInstances {

  type T[A] = EitherK[CatCmd, EitherK[Argument, DirCmd, ?], A]

  println("Starting...")

  val dirTransformer = new DirTransformer[T]()
  val catTransformer = new CatTransformer[T]()
  val argTransformer = new ArgTransformer[T]()

  val transform = Ytransform(Seq(dirTransformer.transform(_), catTransformer.transform(_), argTransformer.transform(_)))

  val expr: Fix[T] = ls[T](List(arg[T]("dir1"), arg[T]("dir2")))

  val eval = Eval.eval[T, State[RunTime, ?]](expr)

  val (runtime, result) = eval.run(RunTime()).value

  println(result)

  //todo use stream
  while (true) {
    val line = StdIn.readLine()
    CmdParser.parse(line) match {
      case Left(err) => println(err)
      case Right(shelExpr) =>
        val fixExpr = transform(shelExpr)
        val eval = Eval.eval[T, State[RunTime, ?]](fixExpr)

        val (runtime, result) = eval.run(RunTime()).value

        println(result)
    }

  }

  //todo 1) pipe combinator ls file | cat
  //todo 2) vfs
  //todo 3) use stream instead of while
}
