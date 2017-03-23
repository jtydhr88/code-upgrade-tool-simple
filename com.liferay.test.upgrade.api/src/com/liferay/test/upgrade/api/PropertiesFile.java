package com.liferay.test.upgrade.api;

import java.io.File;
import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface PropertiesFile {

	List<SearchResult> findProperties(String keys);

	public void setFile(File file);

}
