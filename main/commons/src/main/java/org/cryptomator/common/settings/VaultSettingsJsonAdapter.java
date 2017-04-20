/*******************************************************************************
 * Copyright (c) 2017 Skymatic UG (haftungsbeschränkt).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the accompanying LICENSE file.
 *******************************************************************************/
package org.cryptomator.common.settings;

import java.io.IOException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

class VaultSettingsJsonAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(VaultSettingsJsonAdapter.class);

	public void write(JsonWriter out, VaultSettings value) throws IOException {
		out.beginObject();
		out.name("id").value(value.getId());
		out.name("path").value(value.path().get().toString());
		out.name("mountName").value(value.mountName().get());
		out.name("winDriveLetter").value(value.winDriveLetter().get());
		out.name("mountAfterUnlock").value(value.mountAfterUnlock().get());
		out.name("revealAfterMount").value(value.revealAfterMount().get());
		out.endObject();
	}

	public VaultSettings read(JsonReader in, Settings settings) throws IOException {
		String id = null;
		String path = null;
		String mountName = null;
		String winDriveLetter = null;
		boolean mountAfterUnlock = true;
		boolean revealAfterMount = true;

		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			switch (name) {
			case "id":
				id = in.nextString();
				break;
			case "path":
				path = in.nextString();
				break;
			case "mountName":
				mountName = in.nextString();
				break;
			case "winDriveLetter":
				winDriveLetter = in.nextString();
				break;
			case "mountAfterUnlock":
				mountAfterUnlock = in.nextBoolean();
				break;
			case "revealAfterMount":
				revealAfterMount = in.nextBoolean();
				break;
			default:
				LOG.warn("Unsupported vault setting found in JSON: " + name);
				in.skipValue();
			}
		}
		in.endObject();

		VaultSettings vaultSettings = (id == null) ? VaultSettings.withRandomId(settings) : new VaultSettings(settings, id);
		vaultSettings.mountName().set(mountName);
		vaultSettings.path().set(Paths.get(path));
		vaultSettings.winDriveLetter().set(winDriveLetter);
		vaultSettings.mountAfterUnlock().set(mountAfterUnlock);
		vaultSettings.revealAfterMount().set(revealAfterMount);
		return vaultSettings;
	}

}
