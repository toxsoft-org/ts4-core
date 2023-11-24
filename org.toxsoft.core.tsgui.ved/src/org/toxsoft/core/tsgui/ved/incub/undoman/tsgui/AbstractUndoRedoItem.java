package org.toxsoft.core.tsgui.ved.incub.undoman.tsgui;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoRedoItem} abstract implementation.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractUndoRedoItem
    implements IUndoRedoItem {

  private final IOptionSetEdit params = new OptionSet();
  private final UndoManager    ownerManager;

  /**
   * Constructor.
   *
   * @param aOwnerManager {@link UndoManager} - the owner UNDO manager
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public AbstractUndoRedoItem( UndoManager aOwnerManager, Object... aIdsAndValues ) {
    ownerManager = TsNullArgumentRtException.checkNull( aOwnerManager );
    params.addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  final public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params.getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets {@link #params()} options of visualization
   *
   * @param aName String - the short name
   * @param aDescription String - the description
   * @param aIconId String - iconID or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public void setVisualParams( String aName, String aDescription, String aIconId ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    DDEF_NAME.setValue( params, avStr( aName ) );
    DDEF_DESCRIPTION.setValue( params, avStr( aDescription ) );
    if( aIconId != null ) {
      DDEF_ICON_ID.setValue( params, avStr( aIconId ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // IUndoRedoItem
  //

  @SuppressWarnings( "unchecked" )
  @Override
  public <T extends IUndoManager> T manager() {
    return (T)ownerManager;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must perform UNDO operation.
   */
  protected abstract void undo();

  /**
   * Implementation must perform REDO operation.
   */
  protected abstract void redo();

}
