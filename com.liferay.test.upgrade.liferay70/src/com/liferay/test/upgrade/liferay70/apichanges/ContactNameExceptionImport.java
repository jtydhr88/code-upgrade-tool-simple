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

package com.liferay.test.upgrade.liferay70.apichanges;

import org.osgi.service.component.annotations.Component;

import com.liferay.test.upgrade.api.FileMigrator;
import com.liferay.test.upgrade.liferay70.ImportStatementMigrator;

@Component(property = { "file.extensions=java", }, service = { FileMigrator.class })
public class ContactNameExceptionImport extends ImportStatementMigrator {

	private final static String[] IMPORTS = new String[] {
			"com.liferay.portal.ContactFirstNameException",
			"com.liferay.portal.ContactFullNameException",
			"com.liferay.portal.ContactLastNameException"
	};

	private final static String[] IMPORTS_FIXED = new String[] {
			"com.liferay.portal.kernel.exception.ContactNameException",
			"com.liferay.portal.kernel.exception.ContactNameException",
			"com.liferay.portal.kernel.exception.ContactNameException" };

	public ContactNameExceptionImport() {
		super(IMPORTS, IMPORTS_FIXED);
	}

}
