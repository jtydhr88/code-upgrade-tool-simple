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

package com.liferay.test.upgrade.api;

import java.io.File;

public class SearchResult {

	public final File file;
	public final int startLine;
	public final int startOffset;
	public final int endLine;
	public final int endOffset;

	public SearchResult(File file, int startOffset, int endOffset, int startLine, int endLine) {
		this.file = file;
		this.startOffset = startOffset;
		this.endOffset = endOffset;
		this.startLine = startLine;
		this.endLine = endLine;
	}

}