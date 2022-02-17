package org.toxsoft.core.tsgui.panels.opdefs;

import static org.toxsoft.core.tsgui.panels.opdefs.IPanelOptionSetConstants.*;
import static org.toxsoft.core.tsgui.panels.opdefs.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IPanelOptionSetEdit} implementation.
 *
 * @author hazard157
 */
public class PanelOptionSetEdit
    extends AbstractLazyPanel<Control>
    implements IPanelOptionSetEdit {

  /**
   * Listens to the lost option checkbox state change.
   *
   * @author hazard157
   */
  class ExcludeParamCheckboxSelectionListener
      extends SelectionAdapter {

    private final IDataDef info;

    ExcludeParamCheckboxSelectionListener( IDataDef aInfo ) {
      info = aInfo;
    }

    @Override
    public void widgetSelected( SelectionEvent aE ) {
      processExcludeParamCheckboxToggle( info.id() );
      fireChangeEvent();
    }

  }

  /**
   * VALEDs listener, generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} event.
   */
  private final IValedControlValueChangeListener valedChangeListener = ( aSource, aEditFinished ) -> fireChangeEvent();

  // ------------------------------------------------------------------------------------
  //

  private final GenericChangeEventer                        genericChangeEventer;
  private final IStridablesListEdit<IDataDef>               defs          = new StridablesList<>();
  private final IStringMapEdit<IValedControl<IAtomicValue>> mapValeds     = new StringMap<>();
  private final IStringMapEdit<Label>                       mapLabels     = new StringMap<>();
  private final IStringMapEdit<Button>                      mapCheckboxes = new StringMap<>();
  private final IOptionSetEdit                              values        = new OptionSet();
  private final boolean                                     isLostOpsCheckboxes;
  private final boolean                                     isUnknownOpsLost;
  private final boolean                                     isInitiallyViewer;

  private ScrolledComposite backplane  = null; // persistent backplane
  private TsComposite       valedsGrid = null; // panel for VALEDs is recreted on demand

  private boolean editable = true;

  /**
   * Constructor.
   * <p>
   * If <code>aUneditable</code> = <code>true</code>, then all VALEDs will be created in viewer mode and call to
   * {@link #setEditable(boolean)} will be ignored.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aViewer boolean - the flag to create vviewer panel, not eidtor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelOptionSetEdit( ITsGuiContext aContext, boolean aViewer ) {
    super( aContext );
    genericChangeEventer = new GenericChangeEventer( this );
    isLostOpsCheckboxes = OPDEF_IS_LOST_OPTION_CHECKBOXES.getValue( tsContext().params() ).asBool();
    isUnknownOpsLost = OPDEF_IS_UNKNOWN_OPTIONS_LOST.getValue( tsContext().params() ).asBool();
    isInitiallyViewer = aViewer;
    if( isInitiallyViewer ) {
      editable = false;
    }
  }

  // TODO TRANSLATE

  /**
   * Конструктор редактируеомой панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PanelOptionSetEdit( ITsGuiContext aContext ) {
    this( aContext, false );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new ScrolledComposite( aParent, SWT.H_SCROLL | SWT.V_SCROLL );
    backplane.setExpandHorizontal( true );
    reinitPanelContent();
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void clearPanelContent() {
    if( valedsGrid != null ) {
      mapLabels.clear();
      mapCheckboxes.clear();
      mapValeds.clear();
      valedsGrid.dispose();
      backplane.setContent( null );
      valedsGrid = null;
    }
  }

  /**
   * Creates VALED based on information from known option definition.
   *
   * @param aParent {@link Composite} - parent composite
   * @param aDef {@link IDataDef} - the option definition
   * @return {@link IValedControl} - created VALED
   */
  private IValedControl<IAtomicValue> createValedControl( Composite aParent, IDataDef aDef ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().setValue( OPID_EDITOR_FACTORY_NAME, IAtomicValue.NULL ); // override inherited editor
    ctx.params().addAll( aDef.params() );
    ctx.params().setStr( OPDEF_TOOLTIP_TEXT, aDef.description() );
    IValedControlFactory factory = ValedControlUtils.guessAvEditorFactory( aDef.atomicType(), ctx );
    IValedControl<IAtomicValue> valed = factory.createEditor( ctx );
    valed.createControl( aParent );
    // TODO setup LayoutData depending on VALED options, as in VecLadderLayout
    int horAlogn = SWT.FILL;
    boolean horGrab = true;
    if( OPDEF_IS_WIDTH_FIXED.getValue( valed.params() ).asBool() ) {
      horAlogn = SWT.LEFT;
      horGrab = false;
    }
    GridData gd = new GridData( horAlogn, SWT.CENTER, horGrab, true, 1, 1 );
    valed.getControl().setLayoutData( gd );
    return valed;
  }

  /**
   * Recreates panel content as of options defined in {@link #listOpionDefs()}.
   */
  private void reinitPanelContent() {
    backplane.setLayoutDeferred( true );
    try {
      // clear content
      clearPanelContent();
      if( defs.isEmpty() ) {
        return;
      }
      // create controls Gir of 2 or 3 columns:
      // option nmName | VALED | (optional Lost option Checkbox)
      valedsGrid = new TsComposite( backplane );
      int colsCount = isLostOpsCheckboxes ? 3 : 2;
      valedsGrid.setLayout( new GridLayout( colsCount, false ) );
      // create one row per known option
      for( IDataDef info : defs ) {
        // Label - name of the edited option
        Label l = new Label( valedsGrid, SWT.LEFT );
        mapLabels.put( info.id(), l );
        l.setText( info.nmName() );
        l.setToolTipText( printf( FORMAT_ID_DESCRITPTION, info ) );
        // IValedControl - option value editor
        IValedControl<IAtomicValue> valed = createValedControl( valedsGrid, info );
        valed.eventer().addListener( valedChangeListener );
        mapValeds.put( info.id(), valed );
        valed.setEditable( isEditable() );
        // Button - optional "Lost option" checkbox
        if( isLostOpsCheckboxes ) {
          Button b = new Button( valedsGrid, SWT.CHECK );
          mapCheckboxes.put( info.id(), b );
          String tootip = String.format( FMT_P_EXCLUDE_CHECKBOX, info.nmName() );
          b.setToolTipText( tootip );
          b.addSelectionListener( new ExcludeParamCheckboxSelectionListener( info ) );
          b.setEnabled( isEditable() );
        }
      }
      backplane.setContent( valedsGrid );
      valedsGrid.setSize( valedsGrid.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true, true );
    }
  }

  /**
   * Copies all known option values from {@link #values} to the VALEDs.
   * <p>
   * If known option is absent in {@link #values} then corresponding checkbox is checked.
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void copyValuesToWidgets() {
    // cycle through all known options
    for( String id : defs.keys() ) {
      IDataDef def = defs.getByKey( id );
      IValedControl valed = mapValeds.getByKey( id );
      boolean hasValue = values.hasValue( def ); // flag that known option presennt in the set
      boolean canEdit = hasValue || !isLostOpsCheckboxes; // if no checkboxes editing is allowed
      // set the value from #values or default value
      IAtomicValue av;
      if( hasValue ) {
        av = def.getValue( values );
      }
      else {
        av = def.defaultValue();
      }
      valed.setValue( av );
      valed.setEditable( isEditable() && canEdit );
      setExcludeParamCheckboxState( id, !hasValue );
    }
  }

  /**
   * Copies values from VALEDs to the {@link #values}.
   * <p>
   * The values lost by the checkboxes and invalid values are excluded from the set. Unknown option values remain intact
   * in the {@link #values}.
   */
  private void copyWidgetsToValues() {
    // cycle through known option leaving others intact
    for( String id : defs.keys() ) {
      IDataDef def = defs.getByKey( id );
      IValedControl<IAtomicValue> valed = mapValeds.getByKey( id );
      // remove lost and invalid values others will be copied from VALED
      if( !getExcludeParamCheckboxState( id ) && !valed.canGetValue().isError() ) {
        Object val = valed.getValue();
        def.setValue( values, IAtomicValue.class.cast( val ) );
      }
      else {
        values.removeByKey( id );
      }
    }
  }

  /**
   * Returns the state of the "Lost option" checkbox.
   * <p>
   * If checkboxes are not present in panel then returns <code>false</code>.
   *
   * @param aOpId String - option ID
   * @return boolean - checkbox state
   */
  private boolean getExcludeParamCheckboxState( String aOpId ) {
    if( isLostOpsCheckboxes ) {
      return mapCheckboxes.getByKey( aOpId ).getSelection();
    }
    return false;
  }

  /**
   * Sets the state of the "Lost option" checkbox.
   *
   * @param aOptionId String - option ID
   * @param aExcluded boolean - checkbox state
   */
  private void setExcludeParamCheckboxState( String aOptionId, boolean aExcluded ) {
    if( isLostOpsCheckboxes ) {
      Button cbx = mapCheckboxes.getByKey( aOptionId );
      if( cbx.getSelection() != aExcluded ) {
        cbx.setSelection( aExcluded );
      }
    }
  }

  /**
   * Fires {@link #genericChangeEventer()} event.
   */
  void fireChangeEvent() {
    genericChangeEventer.fireChangeEvent();
  }

  /**
   * Handles user toggles the state of the "Lost option" checkbox.
   *
   * @param aOpId String - option ID
   */
  void processExcludeParamCheckboxToggle( String aOpId ) {
    boolean isExcluded = getExcludeParamCheckboxState( aOpId );
    mapValeds.getByKey( aOpId ).setEditable( !isExcluded );
    mapLabels.getByKey( aOpId ).setEnabled( !isExcluded );
    setExcludeParamCheckboxState( aOpId, isExcluded );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IPanelOptionSet
  //

  @Override
  public IStridablesList<IDataDef> listOpionDefs() {
    return defs;
  }

  @Override
  public void setOptionDefs( IList<IDataDef> aDefs ) {
    TsNullArgumentRtException.checkNulls( aDefs );
    if( defs.equals( aDefs ) ) {
      return;
    }
    defs.setAll( aDefs );
    // renew VALEDs in panel
    reinitPanelContent();
  }

  @Override
  public IOptionSet getValues() {
    copyWidgetsToValues();
    return values;
  }

  @Override
  public void setValues( IOptionSet aValues ) {
    TsNullArgumentRtException.checkNull( aValues );
    if( isUnknownOpsLost ) { // get only known options
      values.clear();
      for( String id : aValues.keys() ) {
        if( defs.hasKey( id ) ) {
          values.setValue( id, aValues.getValue( id ) );
        }
      }
    }
    else {
      values.setAll( aValues );
    }
    // copy values to VALEDs, some known options may NOT be present in values
    copyValuesToWidgets();
  }

  // ------------------------------------------------------------------------------------
  // IPanelOptionSetEdit
  //

  @Override
  public boolean isEditable() {
    return !isInitiallyViewer && editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    // ignore method for panel in viewer mode
    if( isInitiallyViewer ) {
      return;
    }
    /**
     * When viewer mode really changes the VALEDs must be recreated because in general, VALED looks and behaves
     * different in viewer and editor modes.
     */
    if( editable != aEditable ) {
      editable = aEditable;
      reinitPanelContent();
      copyValuesToWidgets();
    }
  }

  @Override
  public ValidationResult validateValues() {
    TsIllegalStateRtException.checkFalse( isControlValid() );
    ValidationResult vr = ValidationResult.SUCCESS;
    for( IDataDef info : defs ) {
      boolean isIncluded = !getExcludeParamCheckboxState( info.id() );
      if( info.isMandatory() && !isIncluded ) {
        vr = ValidationResult.firstNonOk( vr, ValidationResult.error( FMT_ERR_NO_MANDATORY_VALUE, info.id() ) );
        if( vr.isError() ) {
          return vr;
        }
      }
      if( isIncluded ) {
        IValedControl<?> valed = mapValeds.getByKey( info.id() );
        vr = ValidationResult.firstNonOk( vr, valed.canGetValue() );
        if( vr.isError() ) {
          return vr;
        }
      }
    }
    return vr;
  }

}
