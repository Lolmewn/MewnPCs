package nl.lolmewn.mewnpcs.quests;

import nl.lolmewn.mewnpcs.quests.Quest.nextUp;

public class ToDo {
	
	private nextUp next;
	private String value;
	
	public ToDo(nextUp next, String value){
		this.setNext(next);
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	private void setValue(String value) {
		this.value = value;
	}

	public nextUp getNext() {
		return next;
	}

	private void setNext(nextUp next) {
		this.next = next;
	}

}
