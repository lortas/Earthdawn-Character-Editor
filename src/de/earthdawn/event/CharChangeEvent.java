package de.earthdawn.event;

import java.util.EventObject;

public class CharChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	public CharChangeEvent(Object source) {
        super(source);
    }
}

