package com.autentia.tutorials;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FindUnusedAttributesAnalyzerTest {

    private static final String src =
        "public class A {" +
            "private int deadAttribute;" +
            "private int someOtherAttribute;" +
            "public A(int someOtherAttribute) {" +
                "this.someOtherAttribute=someOtherAttribute;" +
            "}" +
            "public void doStuff() {" +
                "this.deadAttribute = 1;" +
                "this.someOtherAttribute = 2;" +
            "}" +
            "public int getOther() {" +
                "return this.someOtherAttribute();" +
            "}" +
        "}";

    private final FindUnusedAttributesAnalyzer findUnusedAttributesAnalyzer = new FindUnusedAttributesAnalyzer();

    @Test
    void shouldDetectDeadAttribute() {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> compilationUnitParseResult = javaParser.parse(src);
        CompilationUnit compilationUnit = compilationUnitParseResult.getResult().get();
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = compilationUnit.getClassByName("A").get();

        List<String> unusedAttributesNames = List.of();
        findUnusedAttributesAnalyzer.visit(classOrInterfaceDeclaration, unusedAttributesNames);

        assertEquals(List.of("deadAttribute"), unusedAttributesNames);
    }
}