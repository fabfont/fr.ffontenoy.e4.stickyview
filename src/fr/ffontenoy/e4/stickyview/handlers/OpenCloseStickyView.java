/**
 * Eclipse Public License - v 1.0
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC LICENSE ("AGREEMENT"). 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * This code has been written by Fabrice Fontenoy and is available from the following git repository:
 * https://github.com/fabfont/fr.ffontenoy.e4.stickyview.git
 */
package fr.ffontenoy.e4.stickyview.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

/**
 * Handler for opening and closing the sticky view
 * 
 * @author Fabrice Fontenoy
 */
public class OpenCloseStickyView {

	/**
	 * Id of the part parameter
	 */
	public static final String PART_ID_PARAMETER = "fr.ffontenoy.e4.stickyview.partId";
	
	/**
	 * Id of the part stack parameter
	 */
	public static final String PART_STACK_ID_PARAMETER = "fr.ffontenoy.e4.stickyview.partStackId";

	@Execute
	public void openCloseStickyView(@Named(PART_STACK_ID_PARAMETER) String pPartStackId, @Named(PART_ID_PARAMETER) String pPartId, EPartService pPartService, EModelService pModelService, MApplication pApplication) {
		
		MPartStack lPartStack = (MPartStack) pModelService.find(pPartStackId, pApplication);
		boolean lVisible = lPartStack.isVisible();
		
		lPartStack.setVisible(!lVisible);
		
		if (!lVisible) {
			pPartService.showPart(pPartId, PartState.ACTIVATE);
		} else {
			MPart lPart = (MPart) pModelService.find(pPartId, pApplication);
			pPartService.hidePart(lPart);
		}
		
	}
}
