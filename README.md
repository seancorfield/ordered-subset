# ordered-subset

An answer to [Adam's latest puzzle](http://blog.adamcameron.me/2015/01/another-quick-code-puzzle-any-language.html).

## Usage

Clone the repo and run: `lein test`

It will take a while because in addition to a few basic assertion tests, it uses test.check - 
a [Clojure version of QuickCheck](https://github.com/clojure/test.check) - 
to ensure that `get-ordered-subset` produces the expected results in a variety of random cases.
Each random invariant test is run fifty times.

## License

Copyright © 2015 Sean Corfield

Distributed under the Eclipse Public License version 1.0.
