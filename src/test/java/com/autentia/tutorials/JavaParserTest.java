package com.autentia.tutorials;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JavaParserTest {

    @Test
    void class_should_be_parsed() {
        InputStream file = this.getClass().getClassLoader().getResourceAsStream("pre/Producto.java");

        CompilationUnit compilationUnit = StaticJavaParser.parse(file);

        assertNotNull(compilationUnit);

        ClassOrInterfaceDeclaration productClass = compilationUnit.getClassByName("Producto").orElseThrow();
        assertEquals("Producto", productClass.getNameAsString());

        assertEquals(1, productClass.getConstructors().size());
        assertTrue(productClass.getMethods().stream().anyMatch(m -> m.getNameAsString().equals("calcularPrecioTotal")));
        assertFieldExistsWithNameAndType(productClass, "nombre", "String");
        assertFieldExistsWithNameAndType(productClass, "precio", PrimitiveType.doubleType().asString());
        assertFieldExistsWithNameAndType(productClass, "cantidad", PrimitiveType.intType().asString());

    }


    private void assertFieldExistsWithNameAndType(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, String name, String typeName) {
        var exists = classOrInterfaceDeclaration
                .getFields()
                .stream()
                .anyMatch(fieldDeclaration ->
                        fieldDeclaration.getElementType().asString().equals(typeName)
                        && existsVariableWithName(fieldDeclaration.getVariables(), name)
                );
        assertTrue(exists);
    }

    private boolean existsVariableWithName(List<VariableDeclarator> fields, String name) {
        return fields.stream().anyMatch(variableDeclarator -> variableDeclarator.getNameAsString().equals(name));
    }
}
