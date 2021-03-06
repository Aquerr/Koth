/*
 * Copyright 2021 Bartłomiej Stępień (Aquerr/Nerdi).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.aquerr.koth.manager;

import io.github.aquerr.koth.util.SelectionPoints;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SelectionManagerImpl implements SelectionManager
{
    private final Map<UUID, SelectionPoints> playerSelectionPoints = new HashMap<>();

    @Override
    public Optional<SelectionPoints> getSelectionPointsForPlayer(Player player)
    {
        return Optional.ofNullable(playerSelectionPoints.get(player.uniqueId()));
    }

    @Override
    public void setSelectionPointsForPlayer(Player player, SelectionPoints selectionPoints)
    {
        this.playerSelectionPoints.put(player.uniqueId(), selectionPoints);
    }

    @Override
    public void removeSelectionPointsForPlayer(Player player)
    {
        this.playerSelectionPoints.remove(player.uniqueId());
    }

    @Override
    public void clear()
    {
        this.playerSelectionPoints.clear();
    }
}
