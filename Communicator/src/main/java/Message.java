public class Message {
	private String type;
	private String field;
	/**
	 * 
	 * @param field
	 */
	public Message(String field) {
		this.type = field.split(",")[0];
		this.field = field;
	}
	public String getType() {
		return type;
	}
	public String getField() {
		return field;
	}

}