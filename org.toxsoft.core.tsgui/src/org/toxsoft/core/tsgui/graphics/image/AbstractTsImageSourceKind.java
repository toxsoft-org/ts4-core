package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsImageSourceKind} implementation base.
 *
 * @author hazard157
 */
public non-sealed abstract class AbstractTsImageSourceKind
    extends StridableParameterized
    implements ITsImageSourceKind {

  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - the image source kind ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractTsImageSourceKind( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // ITsImageSourceKind
  //

  @Override
  final public IStridablesListEdit<IDataDef> opDefs() {
    return opDefs;
  }

  @Override
  final public TsImage createImage( TsImageDescriptor aDescriptor, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aDescriptor, aContext );
    if( !aDescriptor.kindId().equals( id() ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_KIND, aDescriptor.kindId(), id() );
    }
    OptionSetUtils.checkOptionSet( aDescriptor.params(), opDefs );
    TsImage tsim = doCreate( aDescriptor, aContext );
    TsInternalErrorRtException.checkNull( tsim );
    return tsim;
  }

  @Override
  final public TsImageDescriptor editDescription( IOptionSet aParams, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aParams, aContext );
    IOptionSetEdit params = new OptionSet();
    for( IDataDef dd : opDefs ) {
      IAtomicValue av = aParams.findValue( dd.id() );
      if( av != null ) {
        if( AvTypeCastRtException.canAssign( dd.atomicType(), av.atomicType() ) ) {
          params.setValue( dd, av );
        }
      }
    }
    IOptionSet p = doEdit( params, aContext );
    if( p == null ) {
      return null;
    }
    IOptionSetEdit newParams = new OptionSet();
    for( IDataDef dd : opDefs ) {
      IAtomicValue av = p.findValue( dd.id() );
      if( av == null ) {
        av = dd.defaultValue();
      }
      newParams.setValue( dd, av );
    }
    return new TsImageDescriptor( id(), newParams );
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass must create image based on it's description.
   * <p>
   * Arguments are checked for validity: non-<code>null</code>, description is of this kind, and parameters passed
   * validity check against {@link #opDefs}.
   * <p>
   * If image can not be created the method must throw an exception and never return <code>null</code>.
   *
   * @param aDescriptor {@link TsImageDescriptor} - the image descriptor
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link TsImage} - created image
   */
  protected abstract TsImage doCreate( TsImageDescriptor aDescriptor, ITsGuiContext aContext );

  /**
   * Subclass may override and create own implementation of the parameters editing dialog.
   * <p>
   * Default implementation simply calls {@link DialogOptionsEdit#editOpset(ITsDialogInfo, IStridablesList, IOptionSet)}
   * <p>
   * Arguments are checked for validity: non-<code>null</code>, and parameters contains only options listed in
   * {@link #opDefs} with the values of the compatible type.
   *
   * @param aParams {@link IOptionSet} - initial values of the edited options, may be an empty set
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IOptionSet} - edited parameters or <code>null</code>
   */
  protected IOptionSet doEdit( IOptionSet aParams, ITsGuiContext aContext ) {
    String title = String.format( FMT_DLG_T_IMAGE_DESCR_EDIT, nmName() );
    ITsDialogInfo dlgInf = new TsDialogInfo( aContext, STR_DLG_C_IMAGE_DESCR_EDIT, title );
    return DialogOptionsEdit.editOpset( dlgInf, opDefs, aParams );
  }

}
