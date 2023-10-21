package org.toxsoft.core.tsgui.ved.incub.undoman;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoRedoItem} abstract implementation.
 *
 * @author hazard157
 */
public abstract class AbstractUndoRedoItem
    implements IUndoRedoItem {

  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor.
   */
  public AbstractUndoRedoItem() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aName String - the short name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractUndoRedoItem( String aName ) {
    setVisualParams( aName, TsLibUtils.EMPTY_STRING, null );
  }

  /**
   * Constructor.
   *
   * @param aName String - the short name
   * @param aDescription String - the description
   * @param aIconId String - iconID or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractUndoRedoItem( String aName, String aDescription, String aIconId ) {
    setVisualParams( aName, aDescription, aIconId );
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
    DDEF_DEFAULT_VALUE.setValue( params, avStr( aDescription ) );
    if( aIconId != null ) {
      DDEF_ICON_ID.setValue( params, avStr( aIconId ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // IUndoRedoItem
  //

  @Override
  public abstract void undo();

  @Override
  public abstract void redo();

}
