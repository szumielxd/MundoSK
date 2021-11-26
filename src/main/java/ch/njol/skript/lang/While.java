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
 * Copyright Peter Güttinger, SkriptLang team and contributors
 */
package ch.njol.skript.lang;

import org.bukkit.event.Event;
import ch.njol.skript.config.SectionNode;

/**
 * @author Peter Güttinger
 */
public class While extends TriggerSection {
	
	@SuppressWarnings("unused")
	private final Condition c;
	
	public While(final Condition c, final SectionNode n) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	public String toString(final Event e, final boolean debug) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	protected TriggerItem walk(final Event e) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@SuppressWarnings("unused")
	private TriggerItem actualNext;
	
	@Override
	public While setNext(final TriggerItem next) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public TriggerItem getActualNext() {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
}