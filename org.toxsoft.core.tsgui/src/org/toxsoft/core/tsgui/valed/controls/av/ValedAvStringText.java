package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.utils.gui.ITsLibInnerSharedConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#STRING} editor using {@link ValedStringText}.
 *
 * @author hazard157
 */
public class ValedAvStringText
    extends AbstractAvWrapperValedControl<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = TSLIB_VALED_AV_STRING_TEXT;

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
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvStringText( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      return aAtomicType == EAtomicType.STRING;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvStringText( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.STRING, ValedStringText.FACTORY );
    prepareContext( aTsContext );
  }

  static void prepareContext( ITsGuiContext aContext ) {
    aContext.params().setValueIfNull( OPID_IS_HEIGHT_FIXED, AV_TRUE );
    if( ValedStringText.OPDEF_IS_MULTI_LINE.getValue( aContext.params() ).asBool() ) {
      aContext.params().setValueIfNull( OPID_VERTICAL_SPAN, avInt( 3 ) );
    }
    else {
      aContext.params().setValueIfNull( OPID_VERTICAL_SPAN, AV_1 );
    }
  }

  @Override
  protected String av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asString();
  }

  @Override
  protected IAtomicValue tv2av( String aValue ) {
    return AvUtils.avStr( aValue );
  }

}
