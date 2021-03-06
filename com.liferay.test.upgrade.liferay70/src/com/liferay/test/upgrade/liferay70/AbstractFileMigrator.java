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
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;

import com.liferay.test.upgrade.api.FileMigrator;
import com.liferay.test.upgrade.api.SearchResult;

public abstract class AbstractFileMigrator<T> implements FileMigrator {
	BundleContext _context;
	List<String> _fileExtentions;
	final Class<T> _type;

	public AbstractFileMigrator(Class<T> type) {
		_type = type;
	}

	@Activate
	public void activate(ComponentContext ctx) {
		_context = ctx.getBundleContext();

		final Dictionary<String, Object> properties = ctx.getProperties();

		_fileExtentions = Arrays.asList(((String) properties.get("file.extensions")).split(","));
	}

	@Override
	public List<SearchResult> analyze(File file) {
		return searchFile(file, createFileProvider(_type, file));
	}

	protected T createFileProvider(Class<T> type, File file) {
		final String fileExtension = new Path(file.getAbsolutePath()).getFileExtension();

		try {
			final Collection<ServiceReference<T>> refs = _context.getServiceReferences(type,
					"(file.extension=" + fileExtension + ")");

			if (refs != null && refs.size() > 0) {
				ServiceReference<T> sr = refs.iterator().next();

				T service = _context.getService(sr);

				final T fileCheckerFile = type.cast(service);

				if (fileCheckerFile == null) {
					throw new IllegalArgumentException(
							"Could not find " + type.getSimpleName() + " service for specified file " + file.getName());
				}

				return fileCheckerFile;
			}
		} catch (InvalidSyntaxException e) {
		}

		return null;
	}

	protected abstract List<SearchResult> searchFile(File file, T fileChecker);

}
