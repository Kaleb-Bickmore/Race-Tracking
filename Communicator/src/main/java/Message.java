public class Message {
	private String type;
	private String field;
	/**
	 * 
	 * @param type
	 * @param field
	 */
	public Message(String type, String field) {
		this.type = type;
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public String getField() {
		return field;
	}

}