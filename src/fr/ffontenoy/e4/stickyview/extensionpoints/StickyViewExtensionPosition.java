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
 * Enum of posible possition for sticky view
 * 
 * @author Fabrice Fontenoy
 *
 */
public enum StickyViewExtensionPosition {

	/**
	 * Sticky view on the right
	 */
	RIGHT("right"),

	/**
	 * Sticky view on the left
	 */
	LEFT("left"),
	
	/**
	 * Sticky view on top
	 */
	TOP("top"),
	
	/**
	 * Sticky view on bottom
	 */
	BOTTOM ("bottom");
	
	/** Position value */
	private final String mValue; 

	/**
	 * Constructor
	 * 
	 * @param pValue the value
	 */
	private StickyViewExtensionPosition(String pValue) {
		mValue = pValue;
	}
	
	/**
	 * @return the position value
	 */
	public String getValue() {
		return mValue;
	}
	
	/**
	 * Get the enum of the given value
	 * 
	 * @param pValue the value
	 */
	public static StickyViewExtensionPosition getPosition(String pValue) {
		StickyViewExtensionPosition lResult = null;
		switch (pValue) {
		case "right":
			lResult = RIGHT;
			break;
		case "left":
			lResult = LEFT;
			break;
		case "top":
			lResult = TOP;
			break;
		case "bottom":
			lResult = BOTTOM;
			break;
		default:
			break;
		}
		return lResult;
	}
	
}
