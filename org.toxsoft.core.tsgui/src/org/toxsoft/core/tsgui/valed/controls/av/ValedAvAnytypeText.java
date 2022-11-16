package org.toxsoft.core.tsgui.valed.controls.av;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.av.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Any {@link EAtomicType} editor with single line {@link Text}.
 * <p>
 * {@link AvTextParser} is used internally to parse user entered text.
 *
 * @author hazard157
 */
public class ValedAvAnytypeText
    extends AbstractValedControl<IAtomicValue, Text> {

  /**
   * ID of the {@link #OPDEF_ALLOWED_TYPES}.
   */
  public static final String OPID_ALLOWED_TYPES = VALED_OPID_PREFIX + ".AvAnytypeText.AllowedTypes"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_BOOLEAN_TRUE_NAMES}.
   */
  public static final String OPID_BOOLEAN_TRUE_NAMES = VALED_OPID_PREFIX + ".AvAnytypeText.BooleanTrueNames"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_BOOLEAN_FALSE_NAMES}.
   */
  public static final String OPID_BOOLEAN_FALSE_NAMES = VALED_OPID_PREFIX + ".AvAnytypeText.BooleanFalseNames"; //$NON-NLS-1$

  /**
   * ID of the {@link #OPDEF_IS_BLANK_STRING_NULL}.
   */
  public static final String OPID_IS_BLANK_STRING_NULL = VALED_OPID_PREFIX + ".AvAnytypeText.IsBlankStringNull"; //$NON-NLS-1$

  /**
   * Ordered list of allowed atomic type IDs.<br>
   * Default: {@link IAtomicValue#NULL} (is interpreted as {@link AvTextParser#DEFAULT_ALLOWED_TYPES})
   */
  public static final IDataDef OPDEF_ALLOWED_TYPES = DataDef.create( OPID_ALLOWED_TYPES, STRING, //
      TSID_NAME, STR_N_ANYTYPE_TEXT_ALLOWED_TYPES, //
      TSID_DESCRIPTION, STR_D_ANYTYPE_TEXT_ALLOWED_TYPES, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ) //
  );

  /**
   * String constants interpreted as <code>true</code>.<br>
   * Default: {@link AvTextParser#DEFAULT_TRUE_NAMES}
   */
  public static final IDataDef OPDEF_BOOLEAN_TRUE_NAMES = DataDef.create( OPID_BOOLEAN_TRUE_NAMES, VALOBJ, //
      TSID_NAME, STR_N_ANYTYPE_TEXT_BOOLEAN_TRUE_NAMES, //
      TSID_DESCRIPTION, STR_D_ANYTYPE_TEXT_BOOLEAN_TRUE_NAMES, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( AvTextParser.DEFAULT_TRUE_NAMES ) //
  );

  /**
   * String constants interpreted as <code>false</code>.<br>
   * Default: {@link AvTextParser#DEFAULT_FALSE_NAMES}
   */
  public static final IDataDef OPDEF_BOOLEAN_FALSE_NAMES = DataDef.create( OPID_BOOLEAN_FALSE_NAMES, VALOBJ, //
      TSID_NAME, STR_N_ANYTYPE_TEXT_BOOLEAN_FALSE_NAMES, //
      TSID_DESCRIPTION, STR_D_ANYTYPE_TEXT_BOOLEAN_FALSE_NAMES, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( AvTextParser.DEFAULT_FALSE_NAMES ) //
  );

  /**
   * Determines if blank field must be interpreted as {@link IAtomicValue#NULL}, not {@link EAtomicType#STRING}.<br>
   * Default: <code>false</code>
   */
  public static final IDataDef OPDEF_IS_BLANK_STRING_NULL = DataDef.create( OPID_IS_BLANK_STRING_NULL, BOOLEAN, //
      TSID_NAME, STR_N_ANYTYPE_TEXT_IS_EMPTY_STRING_NULL, //
      TSID_DESCRIPTION, STR_D_ANYTYPE_TEXT_IS_EMPTY_STRING_NULL, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvAnytypeText"; //$NON-NLS-1$

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
      return new ValedAvAnytypeText( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  AvTextParser textParser = new AvTextParser();

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedAvAnytypeText( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Обновляет настройки {@link #textParser} из параметров {@link #params()}.
   */
  private void updateTextParser() {
    textParser.setAllowedTypes( getAllowedTypes() );
    textParser.setBooleanTrueNames( getBooleanTrueNames() );
    textParser.setBooleanFalseNames( getBooleanFalseNames() );
    textParser.setEmptyStringNull( getIsEmptyStringNull() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    updateTextParser();
  }

  @Override
  protected Text doCreateControl( Composite aParent ) {
    Text text = new Text( aParent, SWT.BORDER );
    setControl( text );
    updateTextParser();
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      getControl().setEditable( isEditable() );
    }
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return textParser.parse( getControl().getText() );
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    String str = aValue.isAssigned() ? aValue.asString() : TsLibUtils.EMPTY_STRING;
    getControl().setText( str );
  }

  @Override
  protected void doClearValue() {
    getControl().setText( TsLibUtils.EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает значение параметра {@link #OPDEF_ALLOWED_TYPES}.
   *
   * @return IListEAtomicType&gt; - значение параметра {@link #OPDEF_ALLOWED_TYPES}
   */
  public IList<EAtomicType> getAllowedTypes() {
    IStringList atIds = OPDEF_ALLOWED_TYPES.getValue( params() ).asValobj();
    IListEdit<EAtomicType> types = new ElemArrayList<>( atIds.size() );
    for( String s : atIds ) {
      types.add( EAtomicType.findById( s ) );
    }
    return types;
  }

  /**
   * Задает значение параметра {@link #OPDEF_ALLOWED_TYPES}.
   *
   * @param aValue IListEAtomicType&gt; - значение параметра {@link #OPDEF_ALLOWED_TYPES}
   */
  public void setAllowedTypes( IList<EAtomicType> aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    IStringListEdit atIds = new StringArrayList( aValue.size() );
    for( EAtomicType t : aValue ) {
      atIds.add( t.id() );
    }
    OPDEF_ALLOWED_TYPES.setValue( params(), avValobj( atIds ) );
  }

  /**
   * Возвращает значение параметра {@link #OPDEF_BOOLEAN_TRUE_NAMES}.
   *
   * @return IStringList - значение параметра {@link #OPDEF_BOOLEAN_TRUE_NAMES}
   */
  public IStringList getBooleanTrueNames() {
    return OPDEF_BOOLEAN_TRUE_NAMES.getValue( params() ).asValobj();
  }

  /**
   * Задает значение параметра {@link #OPDEF_BOOLEAN_TRUE_NAMES}.
   *
   * @param aValue IStringList - значение параметра {@link #OPDEF_BOOLEAN_TRUE_NAMES}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setBooleanTrueNames( IStringList aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    params().setValobj( OPDEF_BOOLEAN_TRUE_NAMES, aValue );
  }

  /**
   * Возвращает значение параметра {@link #OPDEF_BOOLEAN_FALSE_NAMES}.
   *
   * @return IStringList - значение параметра {@link #OPDEF_BOOLEAN_FALSE_NAMES}
   */
  public IStringList getBooleanFalseNames() {
    return OPDEF_BOOLEAN_FALSE_NAMES.getValue( params() ).asValobj();
  }

  /**
   * Задает значение параметра {@link #OPDEF_BOOLEAN_FALSE_NAMES}.
   *
   * @param aValue IStringList - значение параметра {@link #OPDEF_BOOLEAN_FALSE_NAMES}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setBooleanFalseNames( IStringList aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    params().setValobj( OPDEF_BOOLEAN_FALSE_NAMES, aValue );
  }

  /**
   * Возвращает значение параметра {@link #OPDEF_IS_BLANK_STRING_NULL}.
   *
   * @return boolean - значение параметра {@link #OPDEF_IS_BLANK_STRING_NULL}
   */
  public boolean getIsEmptyStringNull() {
    return OPDEF_IS_BLANK_STRING_NULL.getValue( params() ).asBool();
  }

  /**
   * Задает значение параметра {@link #OPDEF_IS_BLANK_STRING_NULL}.
   *
   * @param aValue boolean - значение параметра {@link #OPDEF_IS_BLANK_STRING_NULL}
   */
  public void setIsEmptyStringNull( boolean aValue ) {
    params().setBool( OPDEF_IS_BLANK_STRING_NULL, aValue );
  }

}
