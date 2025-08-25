import Literal._
import scala.io.StdIn._

object Program {

  def main(args: Array[String]): Unit = {
    println("Write a logical formula...")
    val line = readLine()
    FormulaParser(line) match {
      case Right(f) =>
        println(s"Parsed formula: $f")
        val asCfn: Formula               = CfnTransformer.transform(f)
        val t: TseitinTransformer        = new TseitinTransformer()
        val clauses: List[List[Literal]] = t.encode(f)
        println("========================================")
        println("==============CFN-Standard==============")
        println(s"$asCfn")
        println("========================================")
        println("========================================")
        println("==============CFN-Tseitin==============")
        println(s"${clauses.asString}")
        println("========================================")
      case Left(er) => println(s"You input an invalid logical formula: $er")
    }
  }
}
