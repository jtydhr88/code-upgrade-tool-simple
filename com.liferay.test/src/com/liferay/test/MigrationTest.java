package com.liferay.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.liferay.test.upgrade.api.Migration;
import com.liferay.test.upgrade.api.Problem;

@RunWith(MockitoJUnitRunner.class)
public class MigrationTest {

	private final BundleContext context = FrameworkUtil.getBundle(MigrationTest.class).getBundleContext();

	@Test
	public void testExample() {
		ServiceReference<Migration> sr = context.getServiceReference(Migration.class);

		Migration migration = context.getService(sr);

		assertNotNull(migration);

		File dir = new File("projects/test-portlet/");

		List<Problem> problems = migration.findProblems(dir);

		assertEquals(4, problems.size());
	}

}