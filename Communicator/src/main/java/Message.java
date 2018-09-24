public class Message {
	private String type;
	private String field;

	/**
	 *
	 * @param field This is a string that is comma delimited.
	 */
	public Message(String field) {
		this.type = field.split(",")[0];
		this.field = field;
	}

	/**
	 *
	 * @return the type of the message
	 */
	public String getType() {
		return type;
	}

	/**
	 *
	 * @return the comma delimited field passed into the message constructor
	 */
	public String getField() {
		return field;
	}

}