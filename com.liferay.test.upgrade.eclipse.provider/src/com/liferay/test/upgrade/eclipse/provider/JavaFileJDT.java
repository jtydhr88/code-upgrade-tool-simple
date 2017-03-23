package com.liferay.test.upgrade.eclipse.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.osgi.service.component.annotations.Component;

import com.liferay.test.upgrade.api.JavaFile;
import com.liferay.test.upgrade.api.SearchResult;

@Component(property = { "file.extension=java" }, service = JavaFile.class)
public class JavaFileJDT implements JavaFile {

	private CompilationUnit _ast = null;

	private File _file = null;

	public JavaFileJDT() {
	}

	public JavaFileJDT(File file) {
		setFile(file);
	}

	public void setFile(File file) {
		_file = file;

		try {
			_ast = JDTUtil.createCompilationUnit(file.getName(), JDTUtil.readFile(file));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	@Override
	public List<SearchResult> findImports(String[] imports) {
		final List<SearchResult> searchResults = new ArrayList<>();
		_ast.accept(new ASTVisitor() {

			@Override
			public boolean visit(ImportDeclaration node) {
				String searchContext = node.getName().getFullyQualifiedName();

				for (String importName : imports) {
					if (searchContext.startsWith(importName)) {
						final List<String> importsList = new ArrayList<>(Arrays.asList(imports));

						String greedyImport = importName;
						importsList.remove(importName);

						for (String anotherImport : importsList) {
							if (node.getName().toString().contains(anotherImport)
									&& anotherImport.length() > importName.length()) {
								greedyImport = anotherImport;
							}
						}

						int startLine = _ast.getLineNumber(node.getName().getStartPosition());
						int startOffset = node.getName().getStartPosition();
						int endLine = _ast
								.getLineNumber(node.getName().getStartPosition() + node.getName().getLength());
						int endOffset = node.getName().getStartPosition() + greedyImport.length();

						searchResults.add(new SearchResult(_file, startOffset, endOffset, startLine, endLine));
					}
				}

				return false;
			};
		});

		return searchResults;
	}

}
