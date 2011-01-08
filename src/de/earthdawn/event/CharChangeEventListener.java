package de.earthdawn.event;

import java.util.EventListener;

public interface CharChangeEventListener extends EventListener {
	
	public void CharChanged(CharChangeEvent evt);
}

