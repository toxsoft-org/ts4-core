package org.toxsoft.core.tsgui.ved.editor;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedViselSelectionManager} implementation.
 *
 * @author hazard157
 */
public class VedViselSelectionManager
    implements IVedViselSelectionManager {

  private final IStringListEdit selIdsList = new StringLinkedBundleList();

  private final GenericChangeEventer eventer;
  private final IVedScreen           vedScreen;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to bound this manager to
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselSelectionManager( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    vedScreen = aVedScreen;
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IStringList allViselIds() {
    return vedScreen.model().visels().list().ids();
  }

  private void checkViselExists( String aViselId ) {
    if( !allViselIds().hasElem( aViselId ) ) {
      throw new TsItemNotFoundRtException( aViselId );
    }
  }

  private void checkViselsExists( IStringList aViselIds ) {
    for( String vid : aViselIds ) {
      checkViselExists( vid );
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IVedViselSelectionManager
  //

  @Override
  public ESelectionKind selectionKind() {
    return switch( selIdsList.size() ) {
      case 0 -> ESelectionKind.NONE;
      case 1 -> ESelectionKind.SINGLE;
      default -> ESelectionKind.MULTI;
    };
  }

  @Override
  public IStringList selectedViselIds() {
    return selIdsList;
  }

  @Override
  public String singleSelectedViselId() {
    return selIdsList.findOnly();
  }

  @Override
  public void setSingleSelectedViselId( String aViselId ) {
    checkViselExists( aViselId );
    if( !selIdsList.hasElem( aViselId ) ) {
      selIdsList.add( aViselId );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setViselSelection( String aViselId, boolean aSelection ) {
    if( isSelected( aViselId ) == aSelection ) {
      if( aSelection ) {
        selIdsList.add( aViselId );
      }
      else {
        selIdsList.remove( aViselId );
      }
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setViselsSelection( IStringList aViselIds, boolean aSelection ) {
    checkViselsExists( aViselIds );
    boolean wasChange = false;
    for( String vid : aViselIds ) {
      boolean alreadyHas = selIdsList.hasElem( vid );
      if( aSelection != alreadyHas ) {
        if( aSelection ) {
          selIdsList.add( vid );
        }
        else {
          selIdsList.remove( vid );
        }
        wasChange = true;
      }
    }
    if( wasChange ) {
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void setSelectedVisels( IStringList aSelectedViselIds ) {
    checkViselsExists( aSelectedViselIds );
    if( selIdsList.size() == aSelectedViselIds.size()
        && TsCollectionsUtils.contains( selIdsList, aSelectedViselIds ) ) {
      return;
    }
    selIdsList.setAll( aSelectedViselIds );
    eventer.fireChangeEvent();
  }

  @Override
  public void toggleSelection( String aViselId ) {
    setViselSelection( aViselId, !isSelected( aViselId ) );
  }

  @Override
  public void selectAll() {
    setSelectedVisels( allViselIds() );
  }

  @Override
  public void deselectAll() {
    setSelectedVisels( IStringList.EMPTY );
  }

  @Override
  public IVedScreen vedScreen() {
    return vedScreen;
  }

}
