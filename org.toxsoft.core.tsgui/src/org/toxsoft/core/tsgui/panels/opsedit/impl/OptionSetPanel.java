package org.toxsoft.core.tsgui.panels.opsedit.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.impl.StridUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
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

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aIsViewer boolean - determines if panel will be created in viewer mode
   * @throws TsNullArgumentRtException аргумент = null
   */
  public OptionSetPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext );
    genericEventer = new GenericChangeEventer( this );
    optionEventer = new OptionValueChangeEventer( this );
    viewerMode = aIsViewer;
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
   * Создает редактор значения для параметра aInfo.
   *
   * @param aParent {@link Composite} - родительска панель
   * @param aDef {@link IDataDef} - описание параметра, на основании которого создается редактор
   * @return {@link IValedControl} - созданный редактор
   */
  private IValedControl<IAtomicValue> createValedControl( Composite aParent, IDataDef aDef ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( aDef.params() );
    ctx.params().setValueIfNull( OPDEF_TOOLTIP_TEXT.id(), avStr( aDef.description() ) );
    boolean isReadOnly = ctx.params().getBool( TSID_IS_READ_ONLY, false );
    if( isViewer() || isReadOnly ) {
      ctx.params().setBool( OPDEF_CREATE_UNEDITABLE.id(), true );
    }
    IValedControlFactory factory = ValedControlUtils.guessAvEditorFactory( aDef.atomicType(), ctx );
    IValedControl<IAtomicValue> valed = factory.createEditor( ctx );
    valed.createControl( aParent );
    // TODO настроить LayoutData в зависимости от параметров EValedControlParam, как в VecLadderLayout
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
        Label l = new Label( valedsGrid, SWT.LEFT );
        l.setText( dd.nmName() );
        l.setToolTipText( printf( FORMAT_DESCRIPTION_ID, dd ) );
        // IValedControl - option value editor
        IValedControl<IAtomicValue> valed = createValedControl( valedsGrid, dd );
        valed.eventer().addListener( valedControlValueChangeListener );
        mapValeds.put( dd.id(), valed );
        valed.setEditable( isEditable() );
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
   * Inititlizes {@link #currValues} with default values from {@link #optionDefs}.
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
   * supply value corrresponding eroor will be returned and not all values copied from VALEDs to
   * {@link #currValues}.<br>
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
    genericEventer.fireChangeEvent();
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
    return collectValuesFromValeds();
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
    reinitPanelContent();
    initCurrValuesFromOptionDefs();
    copyCurrValuesToValeds();
    genericEventer.fireChangeEvent();
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
      for( IValedControl<IAtomicValue> c : mapValeds ) {
        c.setEditable( editable );
      }
    }
  }

  @Override
  public ITsEventer<IOptionValueChangeListener> optionChangeEventer() {
    return optionEventer;
  }

}
