/**
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Copyright 2011-2017 Peter Güttinger and contributors
 */
package ch.njol.skript.util.slot;

import ch.njol.skript.lang.Debuggable;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * Represents a container for a single item. It could be an ordinary inventory
 * slot or perhaps an item frame.
 */
public abstract class Slot implements Debuggable {

    protected Slot() {}

    @Nullable
    public abstract ItemStack getItem();

    public abstract void setItem(final @Nullable ItemStack item);

    @Override
    public final String toString() {
        return toString(null, false);
    }

    /**
     * Checks if given slot is in same position with this.
     * Ignores slot contents.
     * @param o Another slot
     * @return True if positions equal, false otherwise.
     */
    public abstract boolean isSameSlot(Slot o);
}