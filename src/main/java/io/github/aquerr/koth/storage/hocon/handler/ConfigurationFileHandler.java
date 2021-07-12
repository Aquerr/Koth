package io.github.aquerr.koth.storage.hocon.handler;

public interface ConfigurationFileHandler<T>
{
    void save(T value);

    T get();

    void reload();
}
