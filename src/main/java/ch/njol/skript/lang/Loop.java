package ch.njol.skript.lang;

import ch.njol.skript.config.SectionNode;
import org.bukkit.event.Event;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class Loop extends TriggerSection {

	@SuppressWarnings("unused")
	private final Expression<?> expr;

	@SuppressWarnings("unused")
	private final transient Map<Event, Object> current = new WeakHashMap<>();
	@SuppressWarnings("unused")
	private final transient Map<Event, Iterator<?>> currentIter = new WeakHashMap<>();
	
	@SuppressWarnings("unused")
	private TriggerItem actualNext;
	
	public Loop(final Expression<?> expr, final SectionNode node) {
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
	
	public Object getCurrent(final Event e) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public Expression<?> getLoopedExpression() {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	@Override
	public Loop setNext(final TriggerItem next) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public TriggerItem getActualNext() {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
	public void exit(Event event) {
		throw new UnsupportedOperationException("This situation couldn't have place");
	}
	
}