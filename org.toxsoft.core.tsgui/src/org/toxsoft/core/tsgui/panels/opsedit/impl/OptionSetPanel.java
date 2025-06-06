package org.toxsoft.core.tsgui.panels.opsedit.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IOptionSetPanel} implementation.
 *
 * @author hazard157
 */
public class OptionSetPanel
    extends AbstractLazyPanel<Control>
    implements IOptionSetPanel {

  /**
   * Listens to the event VALED value changed by user, simple fires {@link #genericChangeEventer()} event.
   */
  private final IValedControlValueChangeListener valedControlValueChangeListener =
      new IValedControlValueChangeListener() {

        @Override
        public void onEditorValueChanged( IValedControl<?> aSource, boolean aEditFinished ) {
          // generate event only on valid and completed edits
          if( aEditFinished && !aSource.canGetValue().isError() ) {
            String optionId = findOptionIdByValed( aSource );
            IAtomicValue av = IAtomicValue.class.cast( aSource.getValue() );
            optionEventer.fireEvent( optionId, av );
            genericEventer.fireChangeEvent();
          }
        }
      };

  private final OptionValueChangeEventer      optionEventer;
  private final IStridablesListEdit<IDataDef> optionDefs = new StridablesList<>();
  private final GenericChangeEventer          genericEventer;

  /**
   * Values as received in #setEntity()
   */
  private final IOptionSetEdit receivedValues = new OptionSet();

  /**
   * Values extracted from the widgets in {@link #canGetEntity()} / {@link #getEntity()}.<br>
   * This values are initialized both in {@link #setEntity(IOptionSet)} and {@link #setOptionDefs(IStridablesList)}.
   * They always contains values for all and only option listed in {@link #listOptionDefs()}.
   */
  private final IOptionSetEdit currValues = new OptionSet();

  /**
   * Values for {@link #isViewer()} as specified in constructor.
   */
  private final boolean viewerMode;

  /**
   * Holds current {@link #isEditable()} state.
   */
  private boolean editable = true;

  /**
   * Permanent composite returned as {@link #getControl()}.
   */
  private ScrolledComposite backplane = null;

  /**
   * Re-creatable grid of widgets.<br>
   * Girls (and included VALEDs) are created from scratch each time when {@link #setOptionDefs(IStridablesList)} changes
   * expected list of options to edit.
   */
  private Composite valedsGrid = null;

  /**
   * VALEDs for all {@link #optionDefs} options editing.
   */
  private final IStringMapEdit<IValedControl<IAtomicValue>> mapValeds = new StringMap<>();

  private final boolean onlyUserEvents;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   * <p>
   * Panel generates change events every time when something changes, either by user in widgets or by programmer via
   * panel API. Setting argument <code>aOnlyUserEvents</code> to <code>true</code> will cause only user input to
   * generate both {@link #genericChangeEventer()} events. Note: {@link #optionChangeEventer()} generates <b>only</b>
   * user input events.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - determines if panel will be created in viewer mode
   * @param aOnlyUserEvents boolean - determines which events to generate
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public OptionSetPanel( ITsGuiContext aContext, boolean aIsViewer, boolean aOnlyUserEvents ) {
    super( aContext );
    genericEventer = new GenericChangeEventer( this );
    optionEventer = new OptionValueChangeEventer( this );
    viewerMode = aIsViewer;
    onlyUserEvents = aOnlyUserEvents;
    if( viewerMode ) {
      editable = false;
    }
  }

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - determines if panel will be created in viewer mode
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public OptionSetPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    this( aContext, aIsViewer, false );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void clearPanelContent() {
    if( valedsGrid != null ) {
      mapValeds.clear();
      valedsGrid.dispose();
      backplane.setContent( null );
      valedsGrid = null;
    }
  }

  /**
   * Creates the VALED editor for option <code>aOpDef</code>.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aOpDef {@link IDataDef} - the option definition
   * @return {@link IValedControl} - created VALED editor
   */
  private IValedControl<IAtomicValue> createValedControl( Composite aParent, IDataDef aOpDef ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IValedControlConstants.inhibitParamsOfParentContext( ctx );
    ctx.params().addAll( aOpDef.params() );
    ctx.params().setValueIfNull( OPDEF_TOOLTIP_TEXT.id(), avStr( aOpDef.description() ) );
    boolean isReadOnly = ctx.params().getBool( TSID_IS_READ_ONLY, false );
    if( isViewer() || isReadOnly ) {
      ctx.params().setBool( OPDEF_CREATE_UNEDITABLE.id(), true );
    }
    IValedControlFactoriesRegistry vcfRegistry = ctx.get( IValedControlFactoriesRegistry.class );
    IValedControlFactory factory = vcfRegistry.getSuitableAvEditor( aOpDef.atomicType(), ctx );
    IValedControl<IAtomicValue> valed = factory.createEditor( ctx );
    valed.createControl( aParent );
    return valed;
  }

  /**
   * Disposes and creates from scratch {@link #valedsGrid} control with widgets according to {@link #optionDefs}.
   */
  private void reinitPanelContent() {
    backplane.setLayoutDeferred( true );
    try {
      clearPanelContent();
      if( optionDefs.isEmpty() ) {
        // may be show message "nothing to edit"?
        return;
      }
      // create grid with 2 columns
      valedsGrid = new TsComposite( backplane );
      valedsGrid.setLayout( new GridLayout( 2, false ) );
      // create one row per option
      for( IDataDef dd : optionDefs ) {
        // Label - option name
        CLabel l = new CLabel( valedsGrid, SWT.CENTER );
        l.setText( dd.nmName() );
        l.setToolTipText( printf( FORMAT_DESCRIPTION_ID, dd ) );
        // VALED - option value editor
        IValedControl<IAtomicValue> valed = createValedControl( valedsGrid, dd );
        valed.eventer().addListener( valedControlValueChangeListener );
        // setup LayoutData according to IValedControlConstants options
        int horAlogn = SWT.FILL;
        boolean horGrab = true;
        if( OPDEF_IS_WIDTH_FIXED.getValue( valed.params() ).asBool() ) {
          horAlogn = SWT.LEFT;
          horGrab = false;
        }
        int verSpan = OPDEF_VERTICAL_SPAN.getValue( valed.params() ).asInt();
        boolean grabVertical = verSpan > 1;
        GridData gd = new GridData( SWT.LEFT, SWT.FILL, false, false, 1, verSpan );
        l.setLayoutData( gd );
        gd = new GridData( horAlogn, SWT.FILL, horGrab, grabVertical, 1, verSpan );
        valed.getControl().setLayoutData( gd );
        // remember created VALED
        mapValeds.put( dd.id(), valed );
      }
      updateValedEditableStatus();
      backplane.setContent( valedsGrid );
      Point valedsGrisSize = valedsGrid.computeSize( SWT.DEFAULT, SWT.DEFAULT );
      valedsGrid.setSize( valedsGrisSize );
      backplane.setMinSize( valedsGrisSize );
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true, true );
    }
  }

  /**
   * Initializes {@link #currValues} with default values from {@link #optionDefs}.
   */
  private void initCurrValuesFromOptionDefs() {
    currValues.clear();
    for( IDataDef dd : optionDefs ) {
      currValues.setValue( dd.id(), dd.defaultValue() );
    }
  }

  /**
   * Copies values from {@link #currValues} to the {@link #mapValeds}.<br>
   * Method assumes {@link #currValues} and {@link #mapValeds} content matches liste option {@link #optionDefs}.
   */
  private void copyCurrValuesToValeds() {
    for( String id : optionDefs.ids() ) {
      IAtomicValue av = currValues.getByKey( id );
      IValedControl<IAtomicValue> valed = mapValeds.getByKey( id );
      valed.setValue( av );
    }
  }

  /**
   * Copies values contained in VALEDs to the {@link #currValues}.<br>
   * Some VALEDs may contain illegal values (ie {@link IValedControl#canGetValue()} returns error. If any VALED can not
   * supply value corresponding error will be returned and not all values copied from VALEDs to {@link #currValues}.<br>
   * It is assumed that {@link #mapValeds}, {@link #currValues} and {@link #optionDefs} has same set of options.
   *
   * @return {@link ValidationResult} - operation result
   */
  private ValidationResult collectValuesFromValeds() {
    ValidationResult vr = ValidationResult.SUCCESS;
    for( String id : optionDefs.ids() ) {
      IValedControl<IAtomicValue> valed = mapValeds.getByKey( id );
      vr = ValidationResult.firstNonOk( vr, valed.canGetValue() );
      if( vr.isError() ) {
        return vr;
      }
      IAtomicValue av = valed.getValue();
      currValues.setValue( id, av );
    }
    return vr;
  }

  /**
   * Updates VALEDs in {@link #mapValeds} editable status depending on {@link #editable} and value of the
   * {@link IAvMetaConstants#TSID_IS_READ_ONLY} option in VALEDs context parameters.
   */
  private void updateValedEditableStatus() {
    for( IValedControl<IAtomicValue> c : mapValeds ) {
      boolean isReadOnly = c.tsContext().params().getBool( TSID_IS_READ_ONLY, false );
      c.setEditable( editable && !isReadOnly );
    }
  }

  private String findOptionIdByValed( IValedControl<?> aValed ) {
    for( String opId : mapValeds.keys() ) {
      IValedControl<?> valed = mapValeds.getByKey( opId );
      if( aValed == valed ) {
        return opId;
      }
    }
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new ScrolledComposite( aParent, SWT.H_SCROLL | SWT.V_SCROLL );
    backplane.setExpandHorizontal( true );
    backplane.setExpandVertical( true );
    reinitPanelContent();
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericEventer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityPanel
  //

  @Override
  public boolean isViewer() {
    return viewerMode;
  }

  @Override
  public void setEntity( IOptionSet aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    receivedValues.setAll( aEntity );
    // init #currValues with default values of listed options
    initCurrValuesFromOptionDefs();
    // copy only listed option from argument to the #currValues
    for( IDataDef dd : optionDefs ) {
      if( aEntity.hasKey( dd.id() ) ) {
        IAtomicValue av = aEntity.getValue( dd.id() );
        currValues.setValue( dd.id(), av );
      }
    }
    // initialize VALEDs with #currValues
    copyCurrValuesToValeds();
    if( !onlyUserEvents ) {
      genericEventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityEditPanel
  //

  @Override
  public IOptionSet getEntity() {
    TsValidationFailedRtException.checkError( collectValuesFromValeds() ); // currValues updated on this call!
    return new OptionSet( currValues );
  }

  @Override
  public ValidationResult canGetEntity() {
    ValidationResult vr = collectValuesFromValeds();
    if( !vr.isError() ) {
      vr = ValidationResult.firstNonOk( vr, OptionSetUtils.validateOptionSet( currValues, optionDefs ) );
    }
    return vr;
  }

  // ------------------------------------------------------------------------------------
  // IOptionSetPanel
  //

  @Override
  public IStridablesList<IDataDef> listOptionDefs() {
    return optionDefs;
  }

  @Override
  public void setOptionDefs( IStridablesList<IDataDef> aDefs ) {
    TsNullArgumentRtException.checkNull( aDefs );
    optionDefs.setAll( aDefs );
    if( backplane != null ) {
      reinitPanelContent();
      initCurrValuesFromOptionDefs();
      copyCurrValuesToValeds();
      if( !onlyUserEvents ) {
        genericEventer.fireChangeEvent();
      }
    }
  }

  @Override
  public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( viewerMode ) {
      return;
    }
    if( editable != aEditable ) {
      editable = aEditable;
      updateValedEditableStatus();
    }
  }

  @Override
  public ITsEventer<IOptionValueChangeListener> optionChangeEventer() {
    return optionEventer;
  }

}
