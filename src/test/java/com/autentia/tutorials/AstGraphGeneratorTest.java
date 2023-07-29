package com.autentia.tutorials;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.DotPrinter;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AstGraphGeneratorTest {

    private final DotPrinter dotPrinter = new DotPrinter(true);

    @Test
    void generate_dot_file() {
        var file = this.getClass().getClassLoader().getResourceAsStream("post/Example.java");
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);

        try (
                FileWriter fileWriter = new FileWriter("Example.dot");
                PrintWriter printWriter = new PrintWriter(fileWriter)
        ) {
            printWriter.print(dotPrinter.output(compilationUnit));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
