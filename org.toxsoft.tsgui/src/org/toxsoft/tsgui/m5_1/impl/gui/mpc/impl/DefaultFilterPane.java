package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import static ru.toxsoft.tsgui.m5.gui.multipane.impl.ITsResources.*;
import static ru.toxsoft.tsgui.utils.icons.IStdIconIds.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMpcFilterPane;
import org.toxsoft.tsgui.widgets.TsComposite;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.txtmatch.ETextMatchMode;

import ru.toxsoft.tsgui.m5.filters.M5FilterTextInFields;
import ru.toxsoft.tslib.polyfilter.*;
import ru.toxsoft.tslib.polyfilter.impl.PolyFilter;
import ru.toxsoft.tslib.utils.misc.*;

/**
 * Реализация {@link IMpcFilterPane} по умолчанию, фильтрующий по тексту в любом столбце просмотрщика.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class DefaultFilterPane<T>
    extends AbstractPane<T, TsComposite>
    implements IMpcFilterPane<T> {

  private final SelectionListener btnFilterSelectionListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      whenWidgetsContentChanged();
    }

  };

  private final ModifyListener textModifyListener = aEvent -> whenWidgetsContentChanged();

  private final SelectionListener btnClearSelectionListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      text.setText( StringUtils.EMPTY_STRING );
    }

  };

  /**
   * Список фабрик, используемый для создания фильтра.
   */
  private static final IStridablesList<ISingleFilterFactory> SFF_LIST =
      new StridablesList<>( M5FilterTextInFields.FACTORY );

  private final GenericChangeEventHelper GenericChangeEventHelper;

  Button btnFilter = null; // кнопка включения/отлючения фильтрации
  Text   text      = null; // используемый для фильтрации текст
  Button btnClear  = null; // кнопка очистки текста

  /**
   * Значение {@link #isFilterOn()}, нужет отдельно, чтобы работало до {@link #createControl(Composite)}.
   */
  private boolean filterOn = false;

  /**
   * Текущий фильтр, может быть {@link IPolyFilter#NULL}, но не бывает <code>null</code>.
   */
  private IPolyFilter filter = IPolyFilter.NULL;

  /**
   * Конструктор.
   *
   * @param aOwner {@link BasicMultiPaneComponent} - компонента, создающая эту панель
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public DefaultFilterPane( BasicMultiPaneComponent<T> aOwner ) {
    super( aOwner );
    GenericChangeEventHelper = new GenericChangeEventHelper( this );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void whenWidgetsContentChanged() {
    filterOn = btnFilter.getSelection();
    filter = IPolyFilter.NULL;
    if( filterOn ) {
      String str = text.getText();
      if( !str.isEmpty() ) {
        IStringList fieldIds = owner().tree().columnManager().columns().ids(); // поля-столбцы, включая невидимые
        ISingleFilterParams sfp = M5FilterTextInFields.createParams( ETextMatchMode.CONTAINS, str, fieldIds );
        filter = PolyFilter.create( sfp, SFF_LIST );
      }
    }
    GenericChangeEventHelper.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducer
  //

  @Override
  public void addGenericChangeListener( IGenericChangeListener aListener ) {
    GenericChangeEventHelper.addGenericChangeListener( aListener );
  }

  @Override
  public void removeGenericChangeListener( IGenericChangeListener aListener ) {
    GenericChangeEventHelper.removeGenericChangeListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация родительских методов
  //

  @Override
  protected TsComposite doCreateControl( Composite aParent ) {
    TsComposite c = new TsComposite( aParent );
    GridLayout gridLayout = new GridLayout( 3, false );
    gridLayout.horizontalSpacing = 1;
    c.setLayout( gridLayout );
    // btnFilter
    btnFilter = new Button( c, SWT.CHECK );
    btnFilter.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    btnFilter.setToolTipText( BTN_P_FILTER );
    ITsIconManager iconManager = owner().tsContext().get( ITsIconManager.class );
    Image icon = iconManager.loadStdIcon( STD_ICONID_VIEW_FILTER, EIconSize.IS_16X16 ); // TODO задать размер значка?
    btnFilter.setImage( icon );
    btnFilter.setSelection( filterOn );
    btnFilter.addSelectionListener( btnFilterSelectionListener );
    // text
    text = new Text( c, SWT.BORDER );
    text.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    text.setMessage( TXT_M_TEXT );
    text.setToolTipText( TXT_P_TEXT );
    text.addModifyListener( textModifyListener );
    // btnClear
    btnClear = new Button( c, SWT.PUSH );
    btnClear.setLayoutData( new GridData( SWT.RIGHT, SWT.FILL, false, false ) );
    // TODO уменьшить размер шрифта
    // btnClear.setText( IGraphicalChars.STR_ERASE_TO_THE_LEFT );

    btnClear.setText( "<" ); //$NON-NLS-1$

    btnClear.setToolTipText( BTN_P_CLEAR );
    btnClear.addSelectionListener( btnClearSelectionListener );
    whenWidgetsContentChanged();
    return c;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFilterPane
  //

  @Override
  public IPolyFilter getFilter() {
    return filter;
  }

  @Override
  public boolean isFilterOn() {
    return filterOn;
  }

  @Override
  public void setFilterOn( boolean aOn ) {
    if( filterOn != aOn ) {
      filterOn = aOn;
      if( btnFilter != null ) {
        btnFilter.setSelection( aOn );
      }
    }
  }

}
