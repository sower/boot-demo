package me.boot.misc;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.io.FileUtils;

/**
 * Parser
 *
 * @since 2025/01/12
 **/
public class Parser {

    public static void main(String[] args) throws FileNotFoundException {
        File file = FileUtils.getFile(
            "D:\\Code\\Mine\\boot-demo\\boot-base\\src\\main\\java\\me\\boot\\base\\parser\\SpelParser.java");
        // 解析代码
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);

        // 遍历 AST 并提取类的字段和方法
        compilationUnit.accept(new ClassVisitor(), null);
    }

    /**
     * 自定义访问者类，用于提取类的字段和方法
     */
    private static class ClassVisitor extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {
            super.visit(classDeclaration, arg);

            // 判断是否是内部类
            if (classDeclaration.isInnerClass()) {
                System.out.println("Inner Class Name: " + classDeclaration.getName());
                System.out.println("Nested in: " + classDeclaration.getParentNode().orElse(null));
                System.out.println("Is Static Inner Class: " + classDeclaration.isStatic());
            } else {
                // 获取类名
                System.out.println("Class Name: " + classDeclaration.getName());
            }


            // 获取所有字段
            System.out.println("\nFields:");
            for (FieldDeclaration field : classDeclaration.getFields()) {
                System.out.println("Field Name: " + field.getVariable(0).getName());
                System.out.println("Field Type: " + field.getElementType());
                System.out.println("Modifiers: " + field.getModifiers());
                System.out.println();
            }

            // 获取所有方法
            System.out.println("\nMethods:");
            for (MethodDeclaration method : classDeclaration.getMethods()) {
                System.out.println("Method Name: " + method.getName());
                System.out.println("Return Type: " + method.getType());
                System.out.println("Parameters: " + method.getParameters());
                System.out.println("Modifiers: " + method.getModifiers());
                System.out.println();
            }
        }
    }
}
