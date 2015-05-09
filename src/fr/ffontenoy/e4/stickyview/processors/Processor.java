/**
 * Eclipse Public License - v 1.0
 * 
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC LICENSE ("AGREEMENT"). 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * This code has been written by Fabrice Fontenoy and is available from the following git repository:
 * https://github.com/fabfont/fr.ffontenoy.e4.stickyview.git
 */
package fr.ffontenoy.e4.stickyview.processors;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindowElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import fr.ffontenoy.e4.stickyview.extensionpoints.StickyViewExtensionAttribute;
import fr.ffontenoy.e4.stickyview.extensionpoints.StickyViewExtensionPosition;
import fr.ffontenoy.e4.stickyview.handlers.OpenCloseStickyView;

/**
 * Sticky view processor for adding application model elements necessary for the
 * sticky view.
 * 
 * @author Fabrice Fontenoy
 */
public class Processor {

	/**
	 * StickyView extension point
	 */
	private static final String EXTENSION_POINT_ID = "fr.ffontenoy.e4.stickyview.stickyviewExtensionPoint";

	/**
	 * Binding context id
	 */
	private static final String BINDING_CONTEXT_ID = "org.eclipse.ui.contexts.dialogAndWindow";

	@Execute
	public void process(MApplication pApplication, EModelService pModelService,
			EPartService pPartService, IExtensionRegistry pExtensionRegistry) {

		boolean lFound = false;
		MWindow lPerspectiveWindow = null;
		MPerspectiveStack lPerspectiveStack = null;
		for (MWindow lWindow : pApplication.getChildren()) {
			for (MWindowElement lWindowElement : lWindow.getChildren()) {
				if (lWindowElement instanceof MPerspectiveStack) {
					lPerspectiveStack = (MPerspectiveStack) lWindowElement;
					System.out.println("Found perspective stack: id = "
							+ lPerspectiveStack.getElementId());
					lFound = true;
					lPerspectiveWindow = lWindow;
					break;

				}
			}
		}

		if (lFound) {
			IConfigurationElement[] lConfigurationElements = pExtensionRegistry
					.getConfigurationElementsFor(EXTENSION_POINT_ID);

			for (IConfigurationElement lConfigurationElement : lConfigurationElements) {
				String lId = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.PART_ID
								.getName());
				String lClass = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.PART_CLASS
								.getName());
				String lContainerData = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.CONTAINER_DATA
								.getName());
				String lPosition = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.POSITION
								.getName());
				String lLabel = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.LABEL
								.getName());
				String lVisible = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.VISIBLE_AT_STARTUP
								.getName());
				String lShortcut = lConfigurationElement
						.getAttribute(StickyViewExtensionAttribute.SHORTCUT
								.getName());

				String lContributor = lConfigurationElement.getContributor()
						.getName();

				MPartSashContainer lPartSashContainer = pModelService
						.createModelElement(MPartSashContainer.class);

				MPartStack lPartStack = pModelService
						.createModelElement(MPartStack.class);
				MPart lPart = MBasicFactory.INSTANCE.createPart();
				lPart.setElementId(lId);
				lPart.setLabel(lLabel);
				lPart.setContributionURI("bundleclass://" + lContributor + "/"
						+ lClass);
				lPartStack.getChildren().add(lPart);
				String lPartStackId = lId + "PartStack";
				lPartStack.setElementId(lPartStackId);
				lPartStack.setContainerData(lContainerData);
				lPartStack.setVisible(Boolean.valueOf(lVisible));

				StickyViewExtensionPosition lPositionValue = StickyViewExtensionPosition
						.getPosition(lPosition);

				switch (lPositionValue) {
				case RIGHT:
					lPartSashContainer.setHorizontal(true);
					lPartSashContainer.getChildren().add(lPerspectiveStack);
					lPartSashContainer.getChildren().add(lPartStack);
					break;
				case LEFT:
					lPartSashContainer.setHorizontal(true);
					lPartSashContainer.getChildren().add(lPartStack);
					lPartSashContainer.getChildren().add(lPerspectiveStack);
					break;
				case BOTTOM:
					lPartSashContainer.setHorizontal(false);
					lPartSashContainer.getChildren().add(lPerspectiveStack);
					lPartSashContainer.getChildren().add(lPartStack);
					break;
				case TOP:
					lPartSashContainer.setHorizontal(false);
					lPartSashContainer.getChildren().add(lPartStack);
					lPartSashContainer.getChildren().add(lPerspectiveStack);
					break;

				default:
					break;
				}

				lPerspectiveWindow.getChildren().add(lPartSashContainer);

				MCommand lCommand = MCommandsFactory.INSTANCE.createCommand();
				lCommand.setCommandName("Open / Close " + lLabel);
				String lCommandId = lId + "CommandId";
				lCommand.setElementId(lCommandId);

				MCommandParameter lPartIdCommandParameter = MCommandsFactory.INSTANCE
						.createCommandParameter();
				lPartIdCommandParameter
						.setElementId(OpenCloseStickyView.PART_ID_PARAMETER);
				lPartIdCommandParameter
						.setName(OpenCloseStickyView.PART_ID_PARAMETER);
				lPartIdCommandParameter.setOptional(false);
				lCommand.getParameters().add(lPartIdCommandParameter);

				MCommandParameter lPartStackIdCommandParameter = MCommandsFactory.INSTANCE
						.createCommandParameter();
				lPartStackIdCommandParameter
						.setElementId(OpenCloseStickyView.PART_STACK_ID_PARAMETER);
				lPartStackIdCommandParameter
						.setName(OpenCloseStickyView.PART_STACK_ID_PARAMETER);
				lPartStackIdCommandParameter.setOptional(false);
				lCommand.getParameters().add(lPartStackIdCommandParameter);

				pApplication.getCommands().add(lCommand);

				MHandler lHandler = MCommandsFactory.INSTANCE.createHandler();
				lHandler.setCommand(lCommand);
				lHandler.setContributionURI("bundle://fr.ffontenoy.e4.stickyview/fr.ffontenoy.e4.stickyview.handlers.OpenCloseStickyView");
				String lHandlerId = lId + "HandlerId";
				lHandler.setElementId(lHandlerId);

				pApplication.getHandlers().add(lHandler);

				MBindingContext lDialogAndWindowBindingContext = null;
				for (MBindingContext lBindingContext : pApplication
						.getBindingContexts()) {
					if (lBindingContext.getElementId().equals(
							BINDING_CONTEXT_ID)) {
						lDialogAndWindowBindingContext = lBindingContext;
					}
				}

				if (lDialogAndWindowBindingContext != null) {
					MBindingTable lBindingTable = MCommandsFactory.INSTANCE
							.createBindingTable();
					lBindingTable
							.setBindingContext(lDialogAndWindowBindingContext);
					lBindingTable.setElementId(lId + "BindingTable");

					MKeyBinding lKeyBinding = MCommandsFactory.INSTANCE
							.createKeyBinding();
					lKeyBinding.setKeySequence(lShortcut);
					lKeyBinding.setCommand(lCommand);

					MParameter lPartIdParameter = MCommandsFactory.INSTANCE
							.createParameter();
					lPartIdParameter
							.setElementId(OpenCloseStickyView.PART_ID_PARAMETER);
					lPartIdParameter
							.setName(OpenCloseStickyView.PART_ID_PARAMETER);
					lPartIdParameter.setValue(lId);
					lKeyBinding.getParameters().add(lPartIdParameter);

					MParameter lPartStackIdParameter = MCommandsFactory.INSTANCE
							.createParameter();
					lPartStackIdParameter
							.setElementId(OpenCloseStickyView.PART_STACK_ID_PARAMETER);
					lPartStackIdParameter
							.setName(OpenCloseStickyView.PART_STACK_ID_PARAMETER);
					lPartStackIdParameter.setValue(lPartStackId);
					lKeyBinding.getParameters().add(lPartStackIdParameter);

					lBindingTable.getBindings().add(lKeyBinding);
					pApplication.getBindingTables().add(lBindingTable);

				}

			}

		} else {
			System.err.println("No perspective stack found");
		}
	}

}