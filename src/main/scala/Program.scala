import Literal._

object Program {

  def main(args: Array[String]): Unit = {
    /*
    FormulaParser("- a & b") match {
      case Right(f) => println(s"Parsed AST: $f")
      case Left(er) => println(s"Did not pass: $er")
    }
    FormulaParser("-(p || q) <-> r") match {
      case Right(f) => println(s"Parsed AST: $f")
      case Left(er) => println(s"Did not pass: $er")
    }
    FormulaParser("a & b || c -> d <-> -e") match {
      case Right(f) => println(s"Parsed AST: $f")
      case Left(er) => println(s"Did not pass: $er")
    }
    */

    FormulaParser("a -> b") match {
      case Right(f) => 
        val t = new TseitinTransformer()
        val clauses: List[List[Literal]] = t.encode(f)
        println(s"Parsed AST: $f")
        println(s"Clauses \n${clauses.asString}")
      case Left(er) => println(s"Error: $er")
    }
  }
}
