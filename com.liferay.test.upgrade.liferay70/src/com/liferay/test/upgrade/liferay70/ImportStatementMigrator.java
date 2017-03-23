/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.test.upgrade.liferay70;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liferay.test.upgrade.api.JavaFile;
import com.liferay.test.upgrade.api.SearchResult;

public abstract class ImportStatementMigrator extends AbstractFileMigrator<JavaFile> {

	private final Map<String, String> _imports = new HashMap<>();

	public ImportStatementMigrator(String[] imports, String[] fixedImports) {
		super(JavaFile.class);

		for (int i = 0; i < imports.length; i++) {
			_imports.put(imports[i], fixedImports[i]);
		}
	}

	@Override
	public List<SearchResult> searchFile(File file, JavaFile javaFile) {
		javaFile.setFile(file);

		return javaFile.findImports(_imports.keySet().toArray(new String[0]));
	}

}
