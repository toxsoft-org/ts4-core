package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.controls.graphics.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Панель выбора цвета для сплошной заливки.
 * <p>
 *
 * @author vs
 */
public class PanelColorFillInfo
    extends TsPanel
    implements IGenericChangeEventCapable {

  RgbaSelector rgbaSelector;

  private final GenericChangeEventer changeEventer;

  IValedControlValueChangeListener valedListener = null;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - соотвествующий контекст
   */
  public PanelColorFillInfo( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    changeEventer = new GenericChangeEventer( this );

    setLayout( new BorderLayout() );

    Composite topPanel = new Composite( this, SWT.NONE );
    topPanel.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    topPanel.setLayoutData( BorderLayout.SOUTH );

    Button btn = new Button( topPanel, SWT.PUSH );
    btn.setText( STR_B_CHOOSE_COLOR );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ColorDialog dlg = new ColorDialog( getShell() );
        RGB rgb = dlg.open();
        if( rgb != null ) {
          rgbaSelector.setRgb( rgb );
        }
      }
    } );

    rgbaSelector = new RgbaSelector( this, SWT.BORDER, aContext.eclipseContext() );
    rgbaSelector.genericChangeEventer().addListener( changeEventer );
    rgbaSelector.setLayoutData( BorderLayout.CENTER );
    if( tsContext().hasKey( IValedControlValueChangeListener.class ) ) {
      rgbaSelector.genericChangeEventer().addListener( aSource -> changeEventer.fireChangeEvent() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return changeEventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает выбранные параметры цвета.<br>
   *
   * @return RGBA - выбранные параметры цвета
   */
  public RGBA rgba() {
    return rgbaSelector.rgba();
  }

  /**
   * Задает параметры цвета.
   *
   * @param aRgba RGBA - параметры цвета
   */
  public void setRgba( RGBA aRgba ) {
    rgbaSelector.setRgba( aRgba );
  }

}
