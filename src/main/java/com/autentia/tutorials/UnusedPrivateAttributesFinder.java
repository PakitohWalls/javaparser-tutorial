package com.autentia.tutorials;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class FindUnusedAttributesAnalyzer extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(AssignExpr expr, List<String> found) {
        super.visit(expr, found);
    }
}
