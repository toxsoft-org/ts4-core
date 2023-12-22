package org.toxsoft.core.tsgui.mws.e4.helpers.partman;

import static org.toxsoft.core.tsgui.mws.e4.helpers.partman.ITsResources.*;

import java.util.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.e4.ui.workbench.modeling.EPartService.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsPartStackManager} implementation.
 * <p>
 * Note of implementation: Fucking Eclipse E4! <br>
 * <ul>
 * <li>{@link EPartService} can not hide part from non-active perspective;</li>
 * <li>{@link EPartService} instance created at application startup is <code>ApplicationPartServiceImpl</code> - it can
 * not even find any part if called from the dialog (when no active context can be found). One needs to get
 * <code>PartServiceImpl</code> instance from the main window context.;</li>
 * <li>So we are using low level {@link EModelService} and {@link MPart} itself to hide parts.</li>
 * </ul>
 *
 * @author hazard157
 */
public class TsPartStackManager
    implements ITsPartStackManager {

  private final IStringMapEdit<MPart> stackParts = new StringMap<>();

  private final MApplication   application;
  private final MTrimmedWindow mainWindow;
  private final EModelService  modelService;
  private final MPartStack     partStack;

  /**
   * Constructor.
   *
   * @param aWinContext {@link IEclipseContext} - windows context
   * @param aPartStackId String - part stack ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such part stack found
   */
  public TsPartStackManager( IEclipseContext aWinContext, String aPartStackId ) {
    TsNullArgumentRtException.checkNulls( aWinContext, aPartStackId );
    modelService = aWinContext.get( EModelService.class );

    application = aWinContext.get( MApplication.class );
    mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, application );
    partStack = (MPartStack)modelService.find( aPartStackId, application );
    TsItemNotFoundRtException.checkNull( partStack, FMT_ERR_NO_SUCH_PART_STACK, aPartStackId );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private MPart internalFindPartInE4Model( String aPartId ) {
    List<MPart> ll = modelService.findElements( application, aPartId, MPart.class );
    if( ll.size() >= 1 ) {
      return ll.get( 0 );
    }
    return null;
  }

  private EPartService partService() {
    EPartService partService = mainWindow.getContext().get( EPartService.class );
    TsInternalErrorRtException.checkNull( partService );
    return partService;
  }

  // ------------------------------------------------------------------------------------
  // ITsPartStackManager
  //

  @Override
  public MPartStack getPartStack() {
    return partStack;
  }

  @Override
  public IStringMap<MPart> listManagedParts() {
    // remove closed UIparts from stackParts if any
    IStringListEdit llClosedPartIds = new StringArrayList();
    for( String pid : stackParts.keys() ) {
      MPart found = internalFindPartInE4Model( pid );
      if( found == null ) {
        llClosedPartIds.add( pid );
      }
    }
    for( String pid : llClosedPartIds ) {
      stackParts.removeByKey( pid );
    }
    //
    return stackParts;
  }

  @Override
  public MPart createPart( UIpartInfo aInfo ) {
    TsNullArgumentRtException.checkNull( aInfo );
    if( listManagedParts().hasKey( aInfo.partId() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_PART_ALREADY_EXISTS, aInfo.partId() );
    }
    MPart part = MBasicFactory.INSTANCE.createPart();
    part.setElementId( aInfo.partId() );
    part.setLabel( aInfo.getLabel() );
    part.setTooltip( aInfo.getTooltip() );
    part.setIconURI( aInfo.getIconUri() );
    part.setContributionURI( aInfo.getContributionUri() );
    part.setCloseable( aInfo.isCloseable() );
    part.getTags().add( EPartService.REMOVE_ON_HIDE_TAG );
    partStack.getChildren().add( part );
    MPart createdPart = partService().showPart( part, PartState.VISIBLE );
    stackParts.put( aInfo.partId(), createdPart );
    return createdPart;
  }

  @Override
  public void closePart( String aPartId ) {
    MPart part = stackParts.getByKey( aPartId );
    MElementContainer<MUIElement> parent = part.getParent();
    part.setToBeRendered( false );
    parent.getChildren().remove( part );

    // partService().hidePart( stackParts.getByKey( aPartId ) );
    stackParts.removeByKey( aPartId );
  }

  @Override
  public void closeAll() {
    for( String partId : stackParts.keys() ) {
      closePart( partId );
    }
  }

}
