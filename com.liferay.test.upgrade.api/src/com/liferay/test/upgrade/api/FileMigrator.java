package com.liferay.test.upgrade.api;

import java.io.File;
import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface FileMigrator {

	public List<SearchResult> analyze(File file);

}
