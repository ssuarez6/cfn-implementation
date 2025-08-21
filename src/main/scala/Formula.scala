sealed trait Formula

case class Var(name: String)             extends Formula
case class Neg(formula: Formula)         extends Formula
case class And(l: Formula, r: Formula)   extends Formula
case class Or(l: Formula, r: Formula)    extends Formula
case class Impl(l: Formula, r: Formula)  extends Formula
case class Equiv(l: Formula, r: Formula) extends Formula
