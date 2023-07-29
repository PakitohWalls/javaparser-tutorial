package com.autentia.tutorials;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetterSetterGeneratorTest {

    private final GetterSetterGenerator getterSetterGenerator = new GetterSetterGenerator();

    @Test
    void should_generate_missing_getter_and_setter() {
        CompilationUnit compilationUnit = loadCompilationUnitFromResourceFile("pre/Example.java");

        CompilationUnit modified = getterSetterGenerator.generate(compilationUnit);

        CompilationUnit expectedCompilationUnit = loadCompilationUnitFromResourceFile("post/Example.java");
        assertEquals(expectedCompilationUnit, modified);
    }

    private CompilationUnit loadCompilationUnitFromResourceFile(String path) {
        var expected = this.getClass().getClassLoader().getResourceAsStream(path);
        return StaticJavaParser.parse(expected);
    }
}
