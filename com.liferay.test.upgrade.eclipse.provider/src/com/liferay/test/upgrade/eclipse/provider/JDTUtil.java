package com.liferay.test.upgrade.eclipse.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class JDTUtil {

	public static char[] readFile(File file) throws FileNotFoundException, IOException {
		String returnValue = null;

		try (FileInputStream stream = new FileInputStream(file)) {
			Reader r = new BufferedReader(new InputStreamReader(stream), 16384);
			StringBuilder result = new StringBuilder(16384);
			char[] buffer = new char[16384];

			int len;

			while ((len = r.read(buffer, 0, buffer.length)) >= 0) {
				result.append(buffer, 0, len);
			}

			returnValue = result.toString();
			r.close();
		}

		return returnValue.toCharArray();
	}

	public static CompilationUnit createCompilationUnit(String unitName, char[] javaSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		@SuppressWarnings("unchecked")
		Map<String, String> options = JavaCore.getOptions();

		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options);

		parser.setCompilerOptions(options);

		// setUnitName for resolve bindings
		parser.setUnitName(unitName);

		String[] sources = { "" };
		String[] classpath = { "" };
		// setEnvironment for resolve bindings even if the args is empty
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);

		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);
		parser.setSource(javaSource);
		parser.setIgnoreMethodBodies(false);

		return (CompilationUnit) parser.createAST(null);
	}
}
