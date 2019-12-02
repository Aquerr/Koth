package io.github.aquerr.koth.config;

import java.util.Collection;
import java.util.List;

public interface Configuration
{
	void reload();

	void save();

	int getInt(int defaultValue, Object... nodePath);

	double getDouble(double defaultValue, Object... nodePath);

	float getFloat(float defaultValue, Object... nodePath);

	boolean getBoolean(boolean defaultValue, Object... nodePath);

	String getString(String defaultValue, Object... nodePath);

	List<String> getListOfStrings(Collection<String> defaultValue, Object... nodePath);

	String getLanguageFileName();

	List<String> getWhiteListedCommandsOnArena();
}
