/*
 * Copyright 2021 Bartłomiej Stępień (Aquerr/Nerdi).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.aquerr.koth.storage;

import io.github.aquerr.koth.storage.hocon.serializer.KothTypeSerializers;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.nio.file.Path;

public class KothConfigurateHelper
{
    private KothConfigurateHelper()
    {

    }

    public static HoconConfigurationLoader loaderForPath(Path path)
    {
        return HoconConfigurationLoader.builder()
                .defaultOptions(getDefaultOptions())
                .path(path)
                .build();
    }

    public static ConfigurationOptions getDefaultOptions()
    {
        return ConfigurationOptions.defaults()
                .serializers(TypeSerializerCollection.defaults()
                    .childBuilder()
                    .registerAll(KothTypeSerializers.getKothTypeSerializers())
                    .build());
    }
}
