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

package com.liferay.test.upgrade.eclipse.provider;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.tracker.ServiceTracker;

import com.liferay.test.upgrade.api.FileMigrator;
import com.liferay.test.upgrade.api.Migration;
import com.liferay.test.upgrade.api.SearchResult;

@Component
public class ProjectMigrationService implements Migration {

	private BundleContext _context;
	private ServiceTracker<FileMigrator, FileMigrator> _fileMigratorTracker;

	@Activate
	public void activate(BundleContext context) {
		_context = context;

		_fileMigratorTracker = new ServiceTracker<FileMigrator, FileMigrator>(context, FileMigrator.class, null);
		_fileMigratorTracker.open();
	}

	protected FileVisitResult analyzeFile(File file, List<SearchResult> problems) {
		String fileName = file.toPath().getFileName().toString();
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

		ServiceReference<FileMigrator>[] fileMigrators = _fileMigratorTracker.getServiceReferences();

		if (fileMigrators != null && fileMigrators.length > 0) {
			for (ServiceReference<FileMigrator> fm : fileMigrators) {

				final List<String> fileExtensions = Arrays
						.asList(((String) fm.getProperty("file.extensions")).split(","));

				if (fileExtensions != null && fileExtensions.contains(extension)) {
					final FileMigrator fmigrator = _context.getService(fm);

					try {
						final List<SearchResult> fileProblems = fmigrator.analyze(file);

						if (fileProblems != null && fileProblems.size() > 0) {
							problems.addAll(fileProblems);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					_context.ungetService(fm);
				}
			}
		}

		return FileVisitResult.CONTINUE;
	}

	@Override
	public List<SearchResult> findProblems(final File projectDir) {
		final List<SearchResult> results = new ArrayList<>();

		walkFiles(projectDir, results);

		return results;
	}

	private void walkFiles(final File dir, final List<SearchResult> results) {
		final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
				File file = path.toFile();

				if (file.isFile()) {
					FileVisitResult result = analyzeFile(file, results);

					if (result.equals(FileVisitResult.TERMINATE)) {
						return result;
					}
				}

				return super.visitFile(path, attrs);
			}
		};

		try {
			Files.walkFileTree(dir.toPath(), visitor);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}