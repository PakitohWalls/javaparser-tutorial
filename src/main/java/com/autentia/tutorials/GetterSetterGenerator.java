package com.autentia.tutorials;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class GetterSetterGenerator {

    private final VoidVisitor<Void> visitor = new VoidVisitorAdapter<>() {


        private final List<Attribute> attributes = new ArrayList<>();
        private final List<String> methodNames = new ArrayList<>();

        @Override
        public void visit(FieldDeclaration n, Void arg) {
            n.getVariables().forEach(v -> this.attributes.add(new Attribute(v.getType(), v.getNameAsString())));
        }

        @Override
        public void visit(MethodDeclaration m, Void arg) {
            methodNames.add(m.getNameAsString());
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            // Primero visitamos el arbol entero para recorrer todos los nodos del AST y que se poblen las variables
            // con la información de la clase.
            super.visit(n, arg);

            System.out.println(attributes);
            System.out.println(methodNames);

            // Despues de recorrer el AST creamos los métodos correspondientes
            attributes.forEach(attribute -> addGetterAndSetterMethods(n, attribute));
        }

        private void addGetterAndSetterMethods(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Attribute attribute) {
            String attributeName = attribute.name;
            String upperCaseVariableName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);

            String getterMethodName = "get" + upperCaseVariableName;
            if (!methodNames.contains(getterMethodName)) {
                MethodDeclaration getter = new MethodDeclaration();
                getter.setName(getterMethodName);
                getter.setType(attribute.type);
                getter.setModifier(Modifier.publicModifier().getKeyword(), true);

                BlockStmt blockStmt = new BlockStmt();

                ReturnStmt returnStmt = new ReturnStmt();
                returnStmt.setExpression(new FieldAccessExpr(new ThisExpr(), attributeName));

                blockStmt.addStatement(returnStmt);

                getter.setBody(blockStmt);

                classOrInterfaceDeclaration.addMember(getter);
            }

            String setterMethodName = "set" + upperCaseVariableName;
            if (!methodNames.contains(setterMethodName)) {
                MethodDeclaration setter = new MethodDeclaration();
                setter.setName(setterMethodName);
                setter.setModifier(Modifier.publicModifier().getKeyword(), true);
                setter.setType(new VoidType());
                setter.addParameter(new Parameter(attribute.type, attributeName));

                BlockStmt blockStmt = new BlockStmt();

                AssignExpr assignExpr = new AssignExpr();
                assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
                assignExpr.setTarget(new FieldAccessExpr(new ThisExpr(), attributeName));
                assignExpr.setValue(new NameExpr(attributeName));

                blockStmt.addStatement(assignExpr);

                setter.setBody(blockStmt);

                classOrInterfaceDeclaration.addMember(setter);
            }
        }

        private record Attribute(Type type, String name) {}
    };

    public CompilationUnit generate(CompilationUnit compilationUnit) {
        compilationUnit.accept(visitor, null);
        return compilationUnit;
    }
}
