# jclingo

This is a small example that creates a low-level binding java to clingo and
calls the `clingo_version()` function as an example.

The example requires clingo to be installed. If clingo has been installed in a
non-standard location, `-DClingo_DIR=<location>` can be passed to help cmake
find it. Similarly, the location of the java installation might have to be
specified. For this, `-DJAVA_HOME=<location>` should be used.

## Example Setup on Ubuntu 20.04

Note that there are two cmake configure/build calls below. The first time the
package is build will result in an error; the second call should run through.
This is because swig generates a lot of java files, which would ideally be
listed in `CMakeLists.txt` file. This can wait till the final version of the
package because I expect this list of files to change a lot during development.

```bash
sudo add-apt-repository ppa:potassco/focal-wip
sudo apt-get update
sudo apt install cmake swig openjdk-14-jdk g++ libclingo-dev
cmake -DJAVA_HOME=/usr/lib/jvm/java-14-openjdk-amd64 -B build
cmake --build build
cmake -DJAVA_HOME=/usr/lib/jvm/java-14-openjdk-amd64 -B build
cmake --build build
```

If everything went right, the example can be executed like this:
```bash
➜ java -Djava.library.path=build -jar build/example.jar
version: 5.5.0
```

## Hint for Development on Windows

Both clingo and swig should fully support Windows but setup on Windows might be
more tricky. An easy alternative is to grab [Ubuntu 20.04 from the Microsoft
store][ms-ubuntu] and follow the above instructions.

[ms-ubuntu]: https://www.microsoft.com/en-us/p/ubuntu-2004-lts/9n6svws3rx71
