package org.toxsoft.core.tsgui.graphics.icons.impl;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Создатель выпадающего меню управления размерами значков {@link IIconSizeable} сущностей.
 *
 * @author hazard157
 * @param <T> - кокрентый класс {@link IIconSizeable} сущности
 */
public class IconSizeableZoomDropDownMenuCreator<T extends IIconSizeable>
    extends AbstractMenuCreator {

  final T              subject;
  final ITsIconManager iconManager;
  final EIconSize      menuIconSize;

  /**
   * Конструктор.
   *
   * @param aSubject &lt;T&gt; - управляемая сущность
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   * @param aMenuIconSize {@link EIconSize} - размер значков в выпадабщем меню
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public IconSizeableZoomDropDownMenuCreator( T aSubject, IEclipseContext aWinContext, EIconSize aMenuIconSize ) {
    TsNullArgumentRtException.checkNulls( aSubject, aWinContext, aMenuIconSize );
    subject = aSubject;
    iconManager = aWinContext.get( ITsIconManager.class );
    menuIconSize = aMenuIconSize;
  }

  /**
   * Конструктор с размеров значков в выпадающем меню {@link EIconSize#IS_16X16}.
   *
   * @param aSubject &lt;T&gt; - управляемая сущность
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public IconSizeableZoomDropDownMenuCreator( T aSubject, IEclipseContext aWinContext ) {
    this( aSubject, aWinContext, EIconSize.IS_16X16 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класска
  //

  @SuppressWarnings( "unused" )
  @Override
  protected boolean fillMenu( Menu aMenu ) {
    // zoom original
    MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( ACDEF_ZOOM_ORIGINAL.nmName() );
    mItem.setToolTipText( ACDEF_ZOOM_ORIGINAL.description() );
    mItem.setImage( iconManager.loadStdIcon( ACDEF_ZOOM_ORIGINAL.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doSetOriginalSize( subject );
      }
    } );
    mItem.setEnabled( subject.iconSize() != subject.defaultIconSize() );
    // separator
    new MenuItem( aMenu, SWT.SEPARATOR );
    // zoom out
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( ACDEF_ZOOM_OUT.nmName() );
    mItem.setToolTipText( ACDEF_ZOOM_OUT.description() );
    mItem.setImage( iconManager.loadStdIcon( ACDEF_ZOOM_OUT.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomOut( subject );
      }
    } );
    mItem.setEnabled( subject.iconSize() != EIconSize.minSize() );
    // zoom in
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( ACDEF_ZOOM_IN.nmName() );
    mItem.setToolTipText( ACDEF_ZOOM_IN.description() );
    mItem.setImage( iconManager.loadStdIcon( ACDEF_ZOOM_IN.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        subject.setIconSize( subject.iconSize().nextSize() );
      }
    } );
    mItem.setEnabled( subject.iconSize() != EIconSize.maxSize() );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Осуществляет сброс масштаба.
   * <p>
   * Метод не вызывается, если текущий размер {@link IIconSizeable#iconSize()} равен
   * {@link IIconSizeable#defaultIconSize()}.
   * <p>
   * В базовом классе устанавливает начальный размер {@link IIconSizeable#defaultIconSize()} управляемой сущности.
   * Вызывать ли ролительский метод при переопределении - зависит от логики использования.
   *
   * @param aSubject &lt;T&gt; - управляемая сущность, не бывает <code>null</code>
   */
  public void doSetOriginalSize( T aSubject ) {
    aSubject.setIconSize( aSubject.defaultIconSize() );
  }

  /**
   * Осуществляет уменьшение масштаба.
   * <p>
   * Метод не вызывается, если текущий размер {@link IIconSizeable#iconSize()} равен {@link EIconSize#minSize()}.
   * <p>
   * В базовом классе устанавливает предыдущий размер {@link EIconSize#prevSize()} управляемой сущности. Вызывать ли
   * ролительский метод при переопределении - зависит от логики использования.
   *
   * @param aSubject &lt;T&gt; - управляемая сущность, не бывает <code>null</code>
   */
  public void doZoomOut( IIconSizeable aSubject ) {
    aSubject.setIconSize( subject.iconSize().prevSize() );
  }

  /**
   * Осуществляет увеличение масштаба.
   * <p>
   * Метод не вызывается, если текущий размер {@link IIconSizeable#iconSize()} равен {@link EIconSize#maxSize()}.
   * <p>
   * В базовом классе устанавливает следующий размер {@link EIconSize#nextSize()} управляемой сущности. Вызывать ли
   * ролительский метод при переопределении - зависит от логики использования.
   *
   * @param aSubject &lt;T&gt; - управляемая сущность, не бывает <code>null</code>
   */
  public void doZoomIn( IIconSizeable aSubject ) {
    aSubject.setIconSize( subject.iconSize().nextSize() );
  }

}
