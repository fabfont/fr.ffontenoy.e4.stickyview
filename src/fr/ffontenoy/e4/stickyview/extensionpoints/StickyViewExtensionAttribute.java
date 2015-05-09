/**
 * Eclipse Public License - v 1.0
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC LICENSE ("AGREEMENT"). 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * This code has been written by Fabrice Fontenoy and is available from the following git repository:
 * https://github.com/fabfont/fr.ffontenoy.e4.stickyview.git
 */
package fr.ffontenoy.e4.stickyview.extensionpoints;

/**
 * Enum of possible attributes for the sticky view extension point
 * 
 * @author Fabrice Fontenoy
 */
public enum StickyViewExtensionAttribute {

	/**
	 * The part id. This part id will be used for the part id but also as a
	 * basis for the part stack id, the command id and the handler id.
	 */
	PART_ID("partId"),

	/**
	 * The label
	 */
	LABEL("label"),

	/**
	 * The class implementing the part. This part id will be used to build the part inside the sticky view.
	 */
	PART_CLASS("partClass"),

	CONTAINER_DATA("containerData"),

	VISIBLE_AT_STARTUP("visibleAtStartup"),

	POSITION("position"),

	SHORTCUT("shortcut");

	/** attibute name */
	private final String mName;

	/**
	 * Constructor
	 * 
	 * @param pName
	 *            the enum name
	 */
	private StickyViewExtensionAttribute(String pName) {
		mName = pName;
	}

	/**
	 * @return the attribute name
	 */
	public String getName() {
		return mName;
	}

}
