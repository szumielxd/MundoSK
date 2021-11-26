package ch.njol.skript.lang;

import org.bukkit.event.Event;

import ch.njol.skript.config.SectionNode;

/**
 * Represents a conditional trigger section.
 * <p>
 * TODO: make this an expression
 * 
 * @author Peter Güttinger
 * @see TriggerSection
 * @see Condition
 */
public class Conditional extends TriggerSection {
	
	@SuppressWarnings("unused")
	private final Condition cond;
	
	@SuppressWarnings("unused")
	private TriggerSection elseClause = null;
	
	public Conditional(final Condition cond, final SectionNode node) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	protected TriggerItem walk(final Event e) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	public String toString(final Event e, final boolean debug) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public void loadElseClause(final SectionNode node) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public void loadElseIf(final Condition cond, final SectionNode n) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public boolean hasElseClause() {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	public Conditional setNext(final TriggerItem next) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	public Conditional setParent(final TriggerSection parent) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
}