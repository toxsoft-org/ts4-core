package org.toxsoft.core.tsgui.valed.controls.metainf;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * VALED edits {@link IDataType} as a whole.
 * <p>
 * Supports following UI outfits:
 * <ul>
 * <li>{@link IValedControlConstants#VALED_UI_OUTFIT_SINGLE_LINE} - the default outfit;</li>
 * <li>{@link IValedControlConstants#VALED_UI_OUTFIT_EMBEDDABLE} - contains atomic type drop-down combo and constraints
 * table to edit {@link IDataType#params()};</li>
 * </ul>
 *
 * @author hazard157
 */
public abstract class ValedDataType
    extends AbstractValedControl<IDataType, Control> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".DataType"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IDataType> doCreateEditor( ITsGuiContext aContext ) {

      // TODO реализовать ValedDataType.Factory.doCreateEditor()
      throw new TsUnderDevelopmentRtException( "ValedDataType.Factory.doCreateEditor()" );

      // IValedControl<IDataType> vc;
      // switch( OPDEF_VALED_UI_OUTFIT.getValue( aContext.params() ).asString() ) {
      // case VALED_UI_OUTFIT_EMBEDDABLE: {
      // // TODO ValedDataType.Factory.doCreateEditor()
      // vc = new ValedDataTypeEmbeddable( aContext );
      // TsTestUtils.pl( "Embed" );
      // }
      // //$FALL-THROUGH$
      // case VALED_UI_OUTFIT_SINGLE_LINE:
      // default: {
      // // TODO ValedDataType.Factory.doCreateEditor()
      // vc = new ValedDataTypeEmbeddable( aContext );
      // TsTestUtils.pl( "Line" );
      // }
      // }
      // return vc;
    }

  }

  protected ValedDataType( ITsGuiContext aContext ) {
    super( aContext );
  }

}
