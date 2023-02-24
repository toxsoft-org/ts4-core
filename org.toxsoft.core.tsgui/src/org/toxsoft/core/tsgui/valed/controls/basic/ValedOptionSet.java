package org.toxsoft.core.tsgui.valed.controls.basic;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IOptionSet} editor implemented with {@link IOptionSetPanel}.
 *
 * @author hazard157
 */
public class ValedOptionSet
    extends AbstractValedControl<IOptionSet, Control> {

  /**
   * ID of the context reference {@link #REFDEF_OPTION_DEFS}.
   */
  public static final String REFID_OPTION_DEFS = VALED_OPID_PREFIX + ".OptionDefs"; //$NON-NLS-1$

  /**
   * Context reference to store {@link IStridablesList}&gt;{@link IDataDef}&lt;.
   * <p>
   * Content of this reference is set be the method {@link #setOptionDefs(IStridablesList)}.
   */
  @SuppressWarnings( "rawtypes" )
  public static final ITsGuiContextRefDef<IStridablesList> REFDEF_OPTION_DEFS =
      TsGuiContextRefDef.create( REFID_OPTION_DEFS, IStridablesList.class );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedOptionSet"; //$NON-NLS-1$

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
    protected IValedControl<IOptionSet> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedOptionSet( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  final IOptionSetPanel panel;

  /**
   * Indicates that option definitions have been set "outside", using the {@link #setOptionDefs(IStridablesList)} method
   * or the VALED options.
   * <p>
   * If option definitions have never been set, definitions will be generated from the data types of the set given by
   * {@link #doSetUnvalidatedValue(IOptionSet)}.
   */
  private boolean isOptionDefsSet = false;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedOptionSet( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
    boolean isViewer = OPDEF_CREATE_UNEDITABLE.getValue( params() ).asBool();
    panel = new OptionSetPanel( tsContext(), isViewer );
    panel.genericChangeEventer().addListener( widgetValueChangeListener );
  }

  private static IStridablesList<IDataDef> prepareDefaultDefs( IOptionSet aValues ) {
    IStridablesListEdit<IDataDef> ll = new StridablesList<>();
    for( String id : aValues.keys() ) {
      IAtomicValue av = aValues.getValue( id );
      IDataDef def = DataDef.create( id, av.atomicType(), //
          TSID_NAME, id, //
          OPDEF_EDITOR_FACTORY_NAME, ValedControlUtils.getDefaultFactoryName( av.atomicType() ) //
      );
      ll.add( def );
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    panel.createControl( aParent );
    return panel.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    panel.setEditable( aEditable );
  }

  @Override
  protected IOptionSet doGetUnvalidatedValue() {
    IOptionSet v = panel.getEntity();
    if( v != null ) {
      return v;
    }
    return IOptionSet.NULL;
  }

  @Override
  protected void doSetUnvalidatedValue( IOptionSet aValue ) {
    if( aValue != null ) {
      // если не заданы описания опции, сгенерируем описания по умолчанию
      if( !isOptionDefsSet ) {
        panel.setOptionDefs( prepareDefaultDefs( aValue ) );
      }
      panel.setEntity( aValue );
    }
    else {
      panel.setEntity( IOptionSet.NULL );
    }
  }

  @Override
  protected void doClearValue() {
    panel.setEntity( IOptionSet.NULL );
  }

  @Override
  public ValidationResult canGetValue() {
    return panel.canGetEntity();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the definitions of the known options.
   * <p>
   * If you set the <code>null</code> argument, then the panel considers that there is no option definitions, resets the
   * internal flag, and when called the method {@link #doSetUnvalidatedValue(IOptionSet)} it will generate default
   * descriptions for all passed values.
   * <p>
   * Setting an empty list just causes no option to be shown.
   *
   * @param aDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions or <code>null</code>
   * @throws TsItemNotFoundRtException не задано имя фабрики редактора для одной из опции
   */
  public void setOptionDefs( IStridablesList<IDataDef> aDefs ) {
    if( aDefs != null ) {
      panel.setOptionDefs( aDefs );
      isOptionDefsSet = true;
    }
    else {
      panel.setOptionDefs( IStridablesList.EMPTY );
      isOptionDefsSet = false;
    }
  }

  /**
   * Returns the definitions of the known options to be displayed (and edited).
   * <p>
   * Edit controls will be displayed in the order of the returned list.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - list of option definitions
   */
  public IStridablesList<IDataDef> getOpionDefs() {
    return panel.listOptionDefs();
  }

}
