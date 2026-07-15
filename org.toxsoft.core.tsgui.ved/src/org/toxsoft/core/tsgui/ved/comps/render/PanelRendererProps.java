package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования свойств отрисовщика с предпросмотром.
 * <p>
 *
 * @author vs
 */
public class PanelRendererProps
    extends AbstractTsDialogPanel<ViselRendererCfg, ITsGuiContext> {

  class FactoriesCombo {

    ComboViewer comboViewer;

    public FactoriesCombo( Composite aParent ) {
      comboViewer = new ComboViewer( aParent, SWT.DROP_DOWN );
      comboViewer.setLabelProvider( new LabelProvider() {

        @Override
        public String getText( Object aElement ) {
          IViselRendererFactory f = (IViselRendererFactory)aElement;
          return f.nmName() + " (" + f.description() + ")"; //$NON-NLS-2$
        }
      } );
      comboViewer.setContentProvider( new ArrayContentProvider() );
      comboViewer.addSelectionChangedListener( aEvent -> {
        ViselRendererCfg cfg = dataRecordInput();
        if( cfg != null ) {
          IStructuredSelection sel = (IStructuredSelection)aEvent.getSelection();
          if( !sel.isEmpty() ) {
            IViselRendererFactory f = (IViselRendererFactory)sel.getFirstElement();
            VedScreen screen = (VedScreen)environ().get( IVedScreen.class );
            String viselId = cfg.viselId();
            VedAbstractVisel visel = screen.model().visels().list().getByKey( viselId );
            // AbstractViselRenderer r = f.create( (ViselRendererCfg)null, visel, screen );
            // ViselRendererCfg newCfg = new ViselRendererCfg( cfg.id(), cfg.kindId(), f.id(), r.props(), visel.id() );
            ViselRendererCfg newCfg = f.createConfig( cfg.id(), visel.id() );
            ITinTypeInfo typeInfo = f.typeInfo();
            ITinValue tv = getTinValue( newCfg, typeInfo );
            tinWidget.setEntityInfo( typeInfo );
            tinWidget.setValue( tv );
            currCfg = newCfg;
          }
        }
      } );
    }

    void setInput( IStridablesList<IViselRendererFactory> aFactories ) {
      comboViewer.setInput( aFactories.toArray() );
    }
  }

  VedRendererFactoriesRegistry registry;
  ViselRendererCfg             currCfg = null;

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  protected PanelRendererProps( Composite aParent, TsDialog<ViselRendererCfg, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    registry = environ().get( VedRendererFactoriesRegistry.class );
    init();
  }

  @Override
  protected void doSetDataRecord( ViselRendererCfg aData ) {
    if( aData != null ) {
      currCfg = aData;
      fCombo.setInput( registry.listFactories( aData.kindId() ) );
      IViselRendererFactory factory = registry.find( aData.factoryId() );
      if( factory != null ) {
        fCombo.comboViewer.setSelection( new StructuredSelection( factory ) );
        ITinTypeInfo typeInfo = factory.typeInfo();
        ITinValue tv = getTinValue( aData, typeInfo );
        tinWidget.setEntityInfo( typeInfo );
        tinWidget.setValue( tv );
      }
    }
  }

  @Override
  protected ViselRendererCfg doGetDataRecord() {
    IStringMap<ITinValue> children = tinWidget.getValue().childValues();
    IOptionSetEdit ops = new OptionSet();
    for( String key : children.keys() ) {
      ITinValue v = children.getByKey( key );
      ops.setValue( key, v.atomicValue() );
    }
    ViselRendererCfg cfg;
    cfg = new ViselRendererCfg( currCfg.id(), currCfg.kindId(), currCfg.factoryId(), ops, currCfg.viselId() );
    VedRendererFactoriesRegistry freg = tsContext().get( VedRendererFactoriesRegistry.class );
    IViselRendererFactory f = freg.find( currCfg.factoryId() );
    // cfg.propValues().setStr( PROPID_NAME, f.getConfigTextRepresentation( cfg ) );
    cfg.params().setStr( PROPID_NAME, f.getConfigTextRepresentation( cfg ) );
    return cfg;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  TinWidget tinWidget;

  Canvas previewPanel;

  FactoriesCombo fCombo;

  void init() {
    this.setLayout( new org.toxsoft.core.tsgui.utils.layout.BorderLayout() );
    Composite topPanel = new Composite( this, SWT.NONE );
    topPanel.setLayout( new GridLayout( 2, false ) );
    CLabel l = new CLabel( topPanel, SWT.CENTER );
    l.setText( "Тип отрисовщика: " );
    fCombo = new FactoriesCombo( topPanel );
    fCombo.comboViewer.getCombo().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    topPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.NORTH );
    Composite bkPane = new Composite( this, SWT.NONE );
    bkPane.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.CENTER );
    bkPane.setLayout( new FillLayout() );
    SashForm sash = new SashForm( bkPane, SWT.BORDER | SWT.HORIZONTAL );
    previewPanel = new Canvas( sash, SWT.BORDER );
    tinWidget = new TinWidget( tsContext() );
    tinWidget.createControl( sash );
    sash.setWeights( 1, 1 );

    tinWidget.addPropertyChangeListener(
        ( aSource, aChangedPropId ) -> System.out.println( "Property: " + aChangedPropId + " changed" ) );
  }

  protected ITinValue getTinValue( ViselRendererCfg aEntity, ITinTypeInfo aTinType ) {
    IOptionSet opSet = aEntity.propValues();
    IStringMapEdit<ITinValue> values = new StringMap<>();
    for( ITinFieldInfo fi : aTinType.fieldInfos() ) {
      if( !opSet.hasKey( fi.id() ) ) {
        System.out.println( fi.id() );
        continue;
      }
      IAtomicValue av = opSet.getValue( fi.id() );
      ITinValue tv;
      if( fi.typeInfo().kind() != ETinTypeKind.ATOMIC ) {
        IStringMap<ITinValue> childVaues = fi.typeInfo().decompose( av );
        tv = TinValue.ofFull( av, childVaues );
      }
      else {
        tv = TinValue.ofAtomic( av );
      }
      values.put( fi.id(), tv );
    }
    return TinValue.ofGroup( values );
  }

  // ------------------------------------------------------------------------------------
  // To use
  //

  /**
   * Статический метод вызова диалога редактирования свойств.
   *
   * @param aContext ViselRendererCfg - конфигурация отрисовщика
   * @param aTsContext {@link ITsGuiContext} - экран редактирования
   * @return Pair&lt;ITinTypeInfo, ITinValue> - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ViselRendererCfg editConfig( ViselRendererCfg aContext, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<ViselRendererCfg, ITsGuiContext> creator = PanelRendererProps::new;
    ITsDialogInfo dlgInfo =
        new TsDialogInfo( aTsContext, "Свойства отрисовщика", "Задайте требуемые значения свойств" );
    TsDialog<ViselRendererCfg, ITsGuiContext> d = new TsDialog<>( dlgInfo, aContext, aTsContext, creator );
    return d.execData();
  }

}
