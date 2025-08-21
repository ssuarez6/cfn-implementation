import Literal._

object Program {

  def main(args: Array[String]): Unit = {
    FormulaParser("(a & b) || -c") match {
      case Right(f) =>
        val t                            = new TseitinTransformer()
        val clauses: List[List[Literal]] = t.encode(f)
        println(s"Parsed AST: $f")
        println(s"Clauses \n${clauses.asString}")
      case Left(er) => println(s"Error: $er")
    }
  }
}
