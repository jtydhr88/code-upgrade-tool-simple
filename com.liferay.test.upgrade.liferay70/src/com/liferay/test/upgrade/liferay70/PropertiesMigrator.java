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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.liferay.test.upgrade.api.PropertiesFile;
import com.liferay.test.upgrade.api.SearchResult;

public abstract class PropertiesMigrator extends AbstractFileMigrator<PropertiesFile> {

	private final Set<String> _keys = new HashSet<>();

	public PropertiesMigrator(String[] keys) {
		super(PropertiesFile.class);

		for (int i = 0; i < keys.length; i++) {
			_keys.add(keys[i]);
		}
	}

	@Override
	public List<SearchResult> searchFile(File file, PropertiesFile propertiesFile) {
		propertiesFile.setFile(file);

		List<SearchResult> sr = new ArrayList<>();

		for (String key : _keys) {
			sr.addAll(propertiesFile.findProperties(key));
		}

		return sr;
	}

}
