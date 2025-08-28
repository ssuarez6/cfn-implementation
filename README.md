# CFN Implementation
A Scala program that parses logical expressions and converts them to their CFN form.
Implemented by Santiago Suarez Perez, for the Logic class.

## Implementation details
Implemented using SBT (Simple build tool) for dependencies management.

This program uses the Scala parser combinators' library, which is supported by the open source community. With the help of this library, formulas as strings will be parsed 
into a `Formula` trait, which defines the AST (Abstract Syntactic Tree) for propositional logic formulas.

Used Gemini to help finish several implementation details.

### Tseitin Transformation

Tseitin transformation is implemented with the guideline provided by algorithm 4.50 in the book, also reinforced by Wikipedia's description of the 
algorithm.

The parsed AST is then passed to the `TseitinConverter.encode` function, which translates any given `Formula` into a CFN form using 
Tseitin's algorithm.

This class stores a state for the variables that need to be created in this transformation process, which is the only place 
in the codebase that uses mutable variables (a.k.a. `var`s).

### CFN transformation

Implementation based on Theorem 4.3 form the book, that recursively traverses the formula adjusting every subformula based on the rules 
of the theorem/algorithm. See `CfnTransformer.scala` to view implementation. 

The `pipeline` defined there composes every step of the algorithm in the standard Scala fashion.

## Execution

### Requirements
* sbt 1.11.4
* openjdk 17.0.4
* scala 2.13.13

In the root directory of the repository, simply run 
```bash
sbt run
```

This will download all the dependencies, compile the classes, and run the main class which in this case is `Program.scala`

## References
- [Mathematical Logic for Computer Science](https://doi.org/10.1007/978-1-4471-4129-7)
- [Scala parser combinators library](https://github.com/scala/scala-parser-combinators?tab=readme-ov-file)
- [SBT](https://www.scala-sbt.org/)
- [Tseitin transformation (Wikipedia)](https://en.wikipedia.org/wiki/Tseytin_transformation)
- [Conjunctive Normal Form (Wikipedia)](https://en.wikipedia.org/wiki/Conjunctive_normal_form)
- [Gemini by Google](https://gemini.google.com/app?hl=es_419)
