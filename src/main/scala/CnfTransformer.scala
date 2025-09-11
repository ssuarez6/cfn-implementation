object CnfTransformer {

  private val pipeline: Formula => Formula =
    simplify _ andThen
      pushNegations _ andThen
      distribute _

  def transform(f: Formula): Formula = pipeline(f)

  def simplify(f: Formula): Formula = f match {
    case Equiv(l, r) => simplify(And(Impl(l, r), Impl(r, l)))
    case Impl(l, r)  => simplify(Or(Neg(l), r))
    case And(l, r)   => And(simplify(l), simplify(r))
    case Or(l, r)    => Or(simplify(l), simplify(r))
    case Neg(s)      => Neg(simplify(s))
    case v: Var      => v
  }

  def pushNegations(f: Formula): Formula = f match {
    case Neg(Neg(f))    => pushNegations(f)
    case Neg(And(l, r)) => Or(pushNegations(Neg(l)), pushNegations(Neg(r)))
    case Neg(Or(l, r))  => And(pushNegations(Neg(l)), pushNegations(Neg(r)))
    case And(l, r)      => And(pushNegations(l), pushNegations(r))
    case Or(l, r)       => Or(pushNegations(l), pushNegations(r))
    case other          => other
  }

  def distribute(f: Formula): Formula = f match {
    case And(l, r) => And(distribute(l), distribute(r))
    case Or(l, r) =>
      val distL = distribute(l)
      val distR = distribute(r)
      (distL, distR) match {
        case (And(l1, l2), rDnf) => And(distribute(Or(l1, rDnf)), distribute(Or(l2, rDnf)))
        case (lDnf, And(r1, r2)) => And(distribute(Or(lDnf, r1)), distribute(Or(lDnf, r2)))
        case (lDnf, rDnf)        => Or(lDnf, rDnf)
      }
    case other => other
  }
}
