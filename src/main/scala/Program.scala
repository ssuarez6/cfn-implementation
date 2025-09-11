import Literal._

object Program {

  def main(args: Array[String]): Unit = {
    //val formula = "(a || b) & (-a || c) & (b || c)"
    val formula = "a & (a -> (b || c)) & (b -> -a) & (c -> -a)"
    FormulaParser(formula) match {
      case Right(f) =>
        println("========================================")
        println(s"Parsed formula: $f")
        val asCnf: Formula               = CnfTransformer.transform(f)
        val clauses: List[Clause] = CnfUtils.formulaCnfToList(asCnf)
        println("========================================")
        println("==============CFN-Standard==============")
        println(s"$asCnf")
        println("========================================")
        println("================Clauses=================")
        println(s"$clauses")
        println("========================================")
        println(s"Result: ${if (DPLL.solve(clauses)) "SAT" else "UNSAT"}")
        //val t: TseitinTransformer        = new TseitinTransformer()
        //val clauses: List[Clause] = t.encode(f)
        //println("========================================")
        //println("==============CFN-Tseitin==============")
        //println(s"${clauses.asString}")
        //println("========================================")
      case Left(er) => println(s"bad formula: $er")
    }
  }
}
