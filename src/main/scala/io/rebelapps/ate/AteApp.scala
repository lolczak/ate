package io.rebelapps.ate

import cats.data.{EitherK, State}
import cats.effect.IO
import cats.mtl.implicits._
import io.rebelapps.ate.interpreter.argument.Argument.arg
import io.rebelapps.ate.interpreter.argument.{ArgTransformer, Argument}
import io.rebelapps.ate.interpreter.cmd.cat.{CatCmd, CatTransformer}
import io.rebelapps.ate.interpreter.cmd.dir.DirCmd.ls
import io.rebelapps.ate.interpreter.cmd.dir.{DirCmd, DirTransformer}
import io.rebelapps.ate.interpreter._
import io.rebelapps.ate.parsing.CmdParser
import io.rebelapps.ate.util.data.Fix
import io.rebelapps.ate.util.functions._
import fs2.Stream

import scala.io.StdIn

object AteApp extends App with HoneypotInstances {

  type T[A] = EitherK[CatCmd, EitherK[Argument, DirCmd, ?], A]

  println("Starting...")

  val transformer = new DirTransformer[T]() |+| new CatTransformer[T]() |+| new ArgTransformer[T]()

  val transform = Y(transformer.transform(_))

//  val expr: Fix[T] = ls[T](List(arg[T]("dir1"), arg[T]("dir2")))
//
//  val eval = Eval.eval[T, State[RunTime, ?]](expr)
//
////  val (runtime, result) = eval.run(RunTime()).value
//
//  println(result)

  val sink: fs2.Pipe[IO, Output, Unit] = _.evalMap {
    case StdOut(line) => IO { println(line) }
    case StdErr(line) => IO { System.err.println(line) }
  }

  val process =
    Stream
      .repeatEval(IO { StdIn.readLine() })
      .mapAccumulate(RunTime()) { case (runtime, line) =>
        CmdParser.parse(line) match {
          case Left(err) => runtime -> Vector(StdErr(err))
          case Right(shelExpr) =>
            val fixExpr = transform(shelExpr)
            val eval = Eval.eval[T, State[RunTime, ?]](fixExpr)

            eval
              .map(_.fold[Vector[Output]](_.results.toVector.map(StdOut.apply))(_.output))
              .run(runtime)
              .value
        }
      }
    .map(_._2)
    .flatMap(Stream.emits)
    .through(sink)
    .compile
    .drain

  process.unsafeRunSync()

  //todo 1) pipe combinator ls file | cat
  //todo 2) vfs
}
