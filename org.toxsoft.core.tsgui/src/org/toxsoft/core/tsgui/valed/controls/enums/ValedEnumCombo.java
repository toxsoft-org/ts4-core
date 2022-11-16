package org.toxsoft.core.tsgui.valed.controls.enums;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The <code>enum</code> values editor from drop-down combo box.
 *
 * @author hazard157
 * @param <V> - the edited enum type
 */
public class ValedEnumCombo<V extends Enum<V>>
    extends AbstractValedEnum<V, Combo> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".EnumCombo"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   * @param <V> - the edited enum type
   */
  static class Factory<V extends Enum<V>>
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<V> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedEnumCombo<>( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory<>();

  private Combo combo;

  /**
   * Constructor with mandatory arguments.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @param aEnumClass {@link Class} - the <code>enum</code> class
   * @param aNameProvider {@link ITsVisualsProvider} - the constants visual representation provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedEnumCombo( ITsGuiContext aTsContext, Class<V> aEnumClass, ITsVisualsProvider<V> aNameProvider ) {
    super( aTsContext, aEnumClass, aNameProvider );
  }

  /**
   * Constructs instance with information from context.
   * <p>
   * The context must contain either {@link IValedEnumConstants#REFDEF_ENUM_CLASS} reference or
   * {@link IValedEnumConstants#OPDEF_ENUM_CLASS_NAME}. Optionally
   * {@link IValedControlConstants#REFDEF_VALUE_VISUALS_PROVIDER} is recoginzed and used if present.
   *
   * @param aTsContext {@link ITsGuiContext} - th editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException context does not contains mandatory information
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedEnumCombo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private boolean isIconDisplayNeeded() {
    EIconSize isonSize = EIconSize.IS_16X16; // TODO механизм задания размеров значков!
    for( V v : items() ) {
      if( visualsProvider().getIcon( v, isonSize ) != null ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedEnum
  //

  @Override
  protected Combo doDoCreateControl( Composite aParent ) {
    combo = new Combo( aParent, SWT.DROP_DOWN | SWT.READ_ONLY );
    combo.addSelectionListener( notificationSelectionListener );
    boolean isIcons = isIconDisplayNeeded();
    for( V v : items() ) {
      combo.add( visualsProvider().getName( v ) );
      if( isIcons ) {
        // TODO use icons from name provider (if present)
      }
    }
    combo.setEnabled( isEditable() );
    return combo;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( combo != null ) {
      combo.setEnabled( aEditable );
    }
  }

  @Override
  protected V doGetUnvalidatedValue() {
    int selIndex = combo.getSelectionIndex();
    if( selIndex < 0 || selIndex >= items().size() ) {
      return items().get( 0 );
    }
    return items().get( selIndex );
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    int index = 0;
    if( aValue != null ) {
      index = items().indexOf( aValue );
    }
    if( index < 0 ) {
      index = 0;
    }
    if( index < items().size() ) {
      combo.select( index );
    }
    else {
      combo.select( -1 );
    }
  }

  @Override
  protected void doClearValue() {
    combo.setText( TsLibUtils.EMPTY_STRING );
  }

}
