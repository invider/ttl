Write a calculator
==================

* platform independent (Linux, MacOS, Windows)
* console mode
* supported operations + - * / %
* works with [double] data type
* REPL (Read/Evaluate/Print Loop)
* eval results are stored in stack
* supports () and operators priorities
* works without ide (run from build/script)

Design
------

// enters main(), create Eval instance,
// do REPL (read line from stdin, Eval.exec(line), print res)
public interface Eval {
    /**
    * evaluate input source, store expression results on stack
    */
    public String exec(String src);
}

public interface Pile {
    public void push(Object val);
    public Object pop();
    public Object peek();
}

How to Start
------------

### project
* install git
* register github account
* create empty git project [ttl]
* create project structure (e.g. ttl/src/main/java/io/ttl)
* create class Total with Hello World in main()
* install build tool (ant+ivy/maven/gradle)
* create the build file (build.xml/pom.xml/gradle.build)
* build the project
* run the app from build tool
* run the app from java main()
* add junit.x.x.x.jar dependency
* create TotalTest.java with a simple test
* run tests
* commit and push changes to git repo
### ide
* install IDE (idea/eclipse...)
* import project to IDE
### calc
* create simple REPL model

