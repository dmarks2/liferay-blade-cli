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

package com.liferay.blade.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Properties;

/**
 * @author Christopher Bryan Boyd
 * @author Gregory Amerson
 */
public class BladeSettings {

	public BladeSettings(File settingsFile) throws IOException {
		_settingsFile = settingsFile;

		if (_settingsFile.exists()) {
			load();
		}
	}

	public Path getCachePath() throws IOException {
		Path userHomePath = _USER_HOME_DIR.toPath();

		Path cachePath = userHomePath.resolve(".blade/cache");

		if (!Files.exists(cachePath)) {
			Files.createDirectories(cachePath);
		}

		return cachePath;
	}

	public Path getExtensionPath() {
		try {
			Path userHomePath = _USER_HOME_DIR.toPath();

			Path dotBladePath = userHomePath.resolve(".blade");

			if (Files.notExists(dotBladePath)) {
				Files.createDirectories(dotBladePath);
			}
			else if (!Files.isDirectory(dotBladePath)) {
				throw new Exception(".blade is not a directory!");
			}

			Path extensions = dotBladePath.resolve("extensions");

			if (Files.notExists(extensions)) {
				Files.createDirectories(extensions);
			}
			else if (!Files.isDirectory(extensions)) {
				throw new Exception(".blade/extensions is not a directory!");
			}

			return extensions;
		}
		catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException(e);
		}
	}

	public String getProfileName() {
		return _properties.getProperty("profile.name");
	}

	public void load() throws IOException {
		try (FileInputStream fileInputStream = new FileInputStream(_settingsFile)) {
			_properties.load(fileInputStream);
		}
	}

	public void save() throws IOException {
		if (!_settingsFile.exists()) {
			File parentDir = _settingsFile.getParentFile();

			parentDir.mkdirs();
		}

		try (FileOutputStream out = new FileOutputStream(_settingsFile)) {
			_properties.store(out, null);
		}
	}

	public void setProfileName(String profileName) {
		_properties.setProperty("profile.name", profileName);
	}

	private static final File _USER_HOME_DIR = new File(System.getProperty("user.home"));

	private final Properties _properties = new Properties();
	private final File _settingsFile;

}