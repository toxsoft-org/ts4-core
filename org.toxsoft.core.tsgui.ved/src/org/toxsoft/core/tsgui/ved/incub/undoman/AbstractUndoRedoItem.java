package org.toxsoft.core.tsgui.ved.incub.undoman;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoRedoItem} base implementation.
 *
 * @author hazard157
 */
public abstract non-sealed class AbstractUndoRedoItem
    implements IUndoRedoItem, IUndoRedoPerformer {

  private final IOptionSetEdit params = new OptionSet();

  private IUndoRedoPerformer redoPerformer = null;

  /**
   * Constructor for subclasses.
   *
   * @param aName String - name to be displayed in undo/redo stack
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException name is a blank string
   */
  protected AbstractUndoRedoItem( String aName ) {
    TsErrorUtils.checkNonBlank( aName );
    DDEF_NAME.setValue( params, avStr( aName ) );
  }

  /**
   * Constructor for subclasses.
   * <p>
   * Creates item with default name.
   */
  protected AbstractUndoRedoItem() {
    this( DEFAULT_NAME );
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  final public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IUndoRedoItem
  //

  @Override
  final public IUndoRedoPerformer undoPerformer() {
    return this;
  }

  @Override
  final public IUndoRedoPerformer redoPerformer() {
    if( redoPerformer == null ) {
      redoPerformer = doCreateRedoPerformar();
      TsInternalErrorRtException.checkNull( redoPerformer );
    }
    return redoPerformer;
  }

  // ------------------------------------------------------------------------------------
  // Methods for subclasses
  //

  /**
   * Sets properties for visual representation of item in undo/redo stack.
   *
   * @param aName String - a non-blank name
   * @param aDescription String - description also used as a tooltip
   * @param aIconId String the iconID, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException name is a blank string
   */
  protected void setVisualProperties( String aName, String aDescription, String aIconId ) {
    TsErrorUtils.checkNonBlank( aName );
    TsNullArgumentRtException.checkNull( aDescription );
    DDEF_NAME.setValue( params, avStr( aName ) );
    DDEF_DEFAULT_VALUE.setValue( params, avStr( aDescription ) );
    if( aIconId != null ) {
      DDEF_ICON_ID.setValue( params, avStr( aIconId ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must perform the operation used to undo editing.
   */
  @Override
  public abstract void doOperation();

  /**
   * Implementation must return performer
   *
   * @return {@link IUndoRedoPerformer} - the performer, must no be null
   */
  protected abstract IUndoRedoPerformer doCreateRedoPerformar();

  @Override
  public void dispose() {
    // nop
  }

}
