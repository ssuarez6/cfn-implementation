# CFN Implementation
A Scala program that parses logical expressions and converts them to their CFN form.
Implemented by Santiago Suarez Perez, for the Logic class.
## Implementation details
Implemented using SBT (Simple build tool) for dependencies management.

This program uses the Scala parser combinators' library, which is supported by the open source community. With the help of this library, formulas as strings will be parsed 
into a `Formula` trait, which defines the AST (Abstract Syntactic Tree) for propositional logic formulas.

This is then passed to the `TseitinConverter.encode` function, which translates any given `Formula` into a CFN form using 
Tseitin's algorithm.

This class stores a state for the variables that need to be created in this transformation process, which is the only place 
in the codebase that uses mutable variables (a.k.a. `var`s).
## Execution

### Requirements
* sbt 1.11.4
* openjdk 17.0.4

In the root directory of the repository, simply run 
```bash
sbt run
```

This will download all the dependencies, compile the classes, and run the main class which in this case is `Program.scala`

## References
- [Scala parser combinators library](https://github.com/scala/scala-parser-combinators?tab=readme-ov-file)
- [SBT](https://www.scala-sbt.org/)
- [Tseitin transformation (Wikipedia)](https://en.wikipedia.org/wiki/Tseytin_transformation)
- [Mathematical Logic for Computer Science](https://doi.org/10.1007/978-1-4471-4129-7)
