package org.toxsoft.core.tsgui.graphics.image;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.graphics.image.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import java.util.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.mws.services.hdpi.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creates thumb size selection drop-down menu for the action {@link #AI_THUMB_SIZEABLE_ZOOM_MENU}.
 * <p>
 * Usage:
 * <ul>
 * <li>add the action {@link #AI_THUMB_SIZEABLE_ZOOM_MENU} to the toolbar;</li>
 * <li>create instance of thei class
 * <code>IMenuCreator thumbSizeMenuCreator = new {@link ThumbSizeableDropDownMenuCreator}(...)</code>;</li>
 * <li>add menu to the action by the method {@link TsToolbar#setActionMenu(String, IMenuCreator) toolbar.setActionMenu(
 * AID_THUMB_SIZEABLE_ZOOM_MENU, thumbSizeMenuCreator )};</li>
 * <li>in the toolbar handler {@link ITsActionHandler#handleAction(String)} handle action with ID
 * #AID_THUMB_SIZEABLE_ZOOM_MENU} by setting default thumb size {@link IThumbSizeable#setThumbSize(EThumbSize)
 * src.setThumbSize( src.defaultThumbSize() )}.</li>
 * </ul>
 * <p>
 * Note: default size {@link IThumbSizeable#defaultThumbSize()} should be in the lsit {@link #getAvailableThumbSizes()}
 * otherwise behavior will be undefined.
 *
 * @author hazrard157
 */
public class ThumbSizeableDropDownMenuCreator
    extends AbstractMenuCreator {

  /**
   * ID of action {@link #AI_THUMB_SIZEABLE_ZOOM_MENU}.
   */
  public static final String AID_THUMB_SIZEABLE_ZOOM_MENU = TS_ID + ".act.ThumbSizeDropDownMenu"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_THUMB_SIZEABLE_ZOOM_IN}.
   */
  public static final String AID_THUMB_SIZEABLE_ZOOM_IN = AID_THUMB_SIZEABLE_ZOOM_MENU + ".ZoomIn"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_THUMB_SIZEABLE_ZOOM_OUT}.
   */
  public static final String AID_THUMB_SIZEABLE_ZOOM_OUT = AID_THUMB_SIZEABLE_ZOOM_MENU + ".ZoomOut"; //$NON-NLS-1$

  // TODO TRANSLATE

  /**
   * Действие с выпадающим меню для управления размером значка {@link IThumbSizeable}.
   * <p>
   * Кнопка вызывает действие {@link ITsStdActionDefs#ACTID_ZOOM_ORIGINAL}, а два пункта выпадающего меню - действия
   * {@link ITsStdActionDefs#ACTID_ZOOM_OUT} и {@link ITsStdActionDefs#ACTID_ZOOM_IN}.
   * <p>
   * <b>Внимание:</b> этому действию нельзя напрямую задавать {@link IMenuCreator}. Надо задать меню тулбару методом
   * {@link TsToolbar#setActionMenu(String, IMenuCreator)}.
   */
  public static final ITsActionDef AI_THUMB_SIZEABLE_ZOOM_MENU = TsActionDef.ofMenu2( AID_THUMB_SIZEABLE_ZOOM_MENU, //
      STR_N_THUMB_SIZEABLE_ZOOM_MENU, STR_D_THUMB_SIZEABLE_ZOOM_MENU, ICONID_ZOOM_ORIGINAL );

  /**
   * Действие увеличение размера миниатюры.
   */
  public static final ITsActionDef AI_THUMB_SIZEABLE_ZOOM_IN = TsActionDef.ofPush2( AID_THUMB_SIZEABLE_ZOOM_IN, //
      STR_N_THUMB_SIZEABLE_ZOOM_IN, STR_D_THUMB_SIZEABLE_ZOOM_IN, ICONID_ZOOM_IN );

  /**
   * Действие уменьшения размера миниатюры.
   */
  public static final ITsActionDef AI_THUMB_SIZEABLE_ZOOM_OUT = TsActionDef.ofPush2( AID_THUMB_SIZEABLE_ZOOM_OUT, //
      STR_N_THUMB_SIZEABLE_ZOOM_OUT, STR_D_THUMB_SIZEABLE_ZOOM_OUT, ICONID_ZOOM_OUT );

  final IThumbSizeable subject;
  final ITsIconManager iconManager;

  final IListEdit<EThumbSize> availableThumbSizes         = new ElemArrayList<>( EThumbSize.values() );
  private boolean             hasIndividualSizesMenuItems = true;
  EIconSize                   menuIconSize                = EIconSize.IS_16X16;

  /**
   * Конструктор.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность
   * @param aContext {@link ITsGuiContext} - the context
   * @param aMenuIconSize {@link EIconSize} - menu icons size or <code>null</code> for ts default size
   * @param aMinThumbSize {@link EThumbSize} - минимальныо допустимый размер миниатюры
   * @param aMaxThumbSize {@link EThumbSize} - максимальныо допустимый размер миниатюры
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ThumbSizeableDropDownMenuCreator( IThumbSizeable aSubject, ITsGuiContext aContext, EIconSize aMenuIconSize,
      EThumbSize aMinThumbSize, EThumbSize aMaxThumbSize ) {
    TsNullArgumentRtException.checkNulls( aSubject, aContext, aMinThumbSize, aMaxThumbSize );
    subject = aSubject;
    iconManager = aContext.get( ITsIconManager.class );
    if( aMenuIconSize != null ) {
      menuIconSize = aMenuIconSize;
    }
    else {
      ITsHdpiService hdpiService = aContext.get( ITsHdpiService.class );
      menuIconSize = hdpiService.getMenuIconsSize();
    }
    setAvalaiableThumbSizesRange( aMinThumbSize, aMaxThumbSize );
  }

  /**
   * Конструктор.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность
   * @param aContext {@link ITsGuiContext} - the context
   * @param aMinThumbSize {@link EThumbSize} - минимальныо допустимый размер миниатюры
   * @param aMaxThumbSize {@link EThumbSize} - максимальныо допустимый размер миниатюры
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ThumbSizeableDropDownMenuCreator( IThumbSizeable aSubject, ITsGuiContext aContext, EThumbSize aMinThumbSize,
      EThumbSize aMaxThumbSize ) {
    this( aSubject, aContext, null, aMinThumbSize, aMaxThumbSize );
  }

  /**
   * Конструктор с размеров значков в выпадающем меню {@link EIconSize#IS_16X16}.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ThumbSizeableDropDownMenuCreator( IThumbSizeable aSubject, ITsGuiContext aContext ) {
    this( aSubject, aContext, null, EThumbSize.minSize(), EThumbSize.maxSize() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класска
  //

  @SuppressWarnings( "unused" )
  @Override
  protected boolean fillMenu( Menu aMenu ) {
    // zoom original
    MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( String.format( FMT_N_ORIGINAL_SIZE, subject.defaultThumbSize().nmName() ) );
    TsSinglesourcingUtils.MenuItem_setToolTipText( mItem,
        String.format( FMT_D_ORIGINAL_SIZE, subject.defaultThumbSize().nmName() ) );
    mItem.setImage( iconManager.loadStdIcon( AI_THUMB_SIZEABLE_ZOOM_MENU.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doSetOriginalSize( subject );
      }
    } );
    mItem.setEnabled( subject.thumbSize() != subject.defaultThumbSize() );
    // separator
    new MenuItem( aMenu, SWT.SEPARATOR );
    // zoom out
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( AI_THUMB_SIZEABLE_ZOOM_OUT.nmName() );
    TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, AI_THUMB_SIZEABLE_ZOOM_OUT.description() );

    mItem.setImage( iconManager.loadStdIcon( AI_THUMB_SIZEABLE_ZOOM_OUT.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomOut( subject );
      }
    } );
    mItem.setEnabled( subject.thumbSize() != getAvailableThumbSizes().first() );
    // zoom in
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( AI_THUMB_SIZEABLE_ZOOM_IN.nmName() );
    TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, AI_THUMB_SIZEABLE_ZOOM_IN.description() );

    mItem.setImage( iconManager.loadStdIcon( AI_THUMB_SIZEABLE_ZOOM_IN.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomIn( subject );
      }
    } );
    mItem.setEnabled( subject.thumbSize() != getAvailableThumbSizes().last() );
    if( hasIndividualSizesMenuItems ) {
      // separator
      new MenuItem( aMenu, SWT.SEPARATOR );
      // availableThumbSizes
      for( EThumbSize sz : availableThumbSizes ) {
        mItem = new MenuItem( aMenu, SWT.CHECK );
        mItem.setText( sz.nmName() );
        TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, sz.description() );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            doSetThumbSize( subject, sz );
          }
        } );
        mItem.setSelection( sz == subject.thumbSize() );
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает размер значков в созданном меню.
   *
   * @return {@link EIconSize} - размер значков в меню
   */
  public EIconSize getMenuIconSize() {
    return menuIconSize;
  }

  /**
   * Задает размер значков в созданном меню.
   *
   * @param aMenuIconSize {@link EIconSize} - размер значков в меню
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setMenuIconSize( EIconSize aMenuIconSize ) {
    menuIconSize = TsNullArgumentRtException.checkNull( aMenuIconSize );
  }

  /**
   * Определяет, будет ли в меню пункт индивидуальные пункты задания размера для каждого из
   * {@link #getAvailableThumbSizes()}.
   *
   * @return boolean - признак наличия индивидуальных пунктов меню допустимых размеров
   */
  public boolean isThumbSizesMenuItems() {
    return hasIndividualSizesMenuItems;
  }

  /**
   * Задает, будет ли в меню пункт индивидуальные пункты задания размера для каждого из
   * {@link #getAvailableThumbSizes()}.
   *
   * @param aValue boolean - признак наличия индивидуальных пунктов меню допустимых размеров
   */
  public void setThumbSizesMenuItems( boolean aValue ) {
    hasIndividualSizesMenuItems = aValue;
  }

  /**
   * Возвращает раземры миниатюр, которые будут использоваться для изменения размеров.
   * <p>
   * Возвращаемый список всегда содержит хотя бы один элемент и всегда отсортирован по учеличениу размеров.
   *
   * @return {@link IList}&lt;{@link EThumbSize}&gt; - список допустимых размеров
   */
  public IList<EThumbSize> getAvailableThumbSizes() {
    return availableThumbSizes;
  }

  /**
   * Задает размеры миниатюр, которые будут использоваться для изменения размеров.
   * <p>
   * Можно указывать люые размеры, диапазон будет свормирован автоматически, даже если aMinSize < aMaxSize. Можно
   * указывать однаковый минимум и максимум - но это не имеет смысла.
   * <p>
   * Внимание: среди задаваемых размеров должен быть размер по умолчанию {@link IThumbSizeable#defaultThumbSize()}.
   *
   * @param aMinSize {@link EThumbSize} - нижняя граница диапазона
   * @param aMaxSize {@link EThumbSize} - верхняя граница диапазона
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setAvalaiableThumbSizesRange( EThumbSize aMinSize, EThumbSize aMaxSize ) {
    TsNullArgumentRtException.checkNulls( aMinSize, aMaxSize );
    availableThumbSizes.clear();
    EThumbSize minSize = aMinSize;
    EThumbSize maxSize = aMaxSize;
    if( minSize.size() > maxSize.size() ) {
      minSize = aMaxSize;
      maxSize = aMinSize;
    }
    EThumbSize sz = minSize;
    while( true ) {
      availableThumbSizes.add( sz );
      if( sz == maxSize ) {
        break;
      }
      sz = sz.nextSize();
    }
  }

  /**
   * Задает размеры миниатюр, которые будут использоваться для изменения размеров.
   * <p>
   * Надо задать хотя бы один размер. Размеры можно указывать в произвольном порядке, они буду отсортированы.
   * <p>
   * Внимание: среди задаваемых размеров должен быть размер по умолчанию {@link IThumbSizeable#defaultThumbSize()}.
   *
   * @param aSizes {@link EThumbSize}[] - массив размеров
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент пусто массив
   */
  public void setAvalaiableThumbSizes( EThumbSize... aSizes ) {
    TsErrorUtils.checkArrayArg( aSizes, 1 );
    Arrays.sort( aSizes );
    availableThumbSizes.setAll( aSizes );
  }

  /**
   * Возвращает следующий (больший) допустимый размер миниатюры.
   * <p>
   * Смысл метода в том, что он возвращает размеры только из допустимых {@link #getAvailableThumbSizes()}. Если аргумент
   * и так наибольший допустимый, то возвращает аргумент.
   * <p>
   * Если аргумент - больше наибольшего допустимого, то возвращает наибольший допустимый размер, который <b>меньше</b>
   * аргумента!
   *
   * @param aThumbSize {@link EThumbSize} - исходный размер
   * @return {@link EThumbSize} - больший допустимый размер, может быть <b>меньше</b> аргумента!
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EThumbSize getNextThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( availableThumbSizes.size() == 1 ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() < availableThumbSizes.first().size() ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() >= availableThumbSizes.last().size() ) {
      return availableThumbSizes.last();
    }
    for( EThumbSize sz = aThumbSize; sz.size() < availableThumbSizes.last().size(); sz = sz.nextSize() ) {
      if( availableThumbSizes.hasElem( sz.nextSize() ) ) {
        return sz.nextSize();
      }
    }
    throw new TsInternalErrorRtException();
  }

  /**
   * Возвращает предыдущий (меньший) допустимый размер миниатюры.
   * <p>
   * Смысл метода в том, что он возвращает размеры только из допустимых {@link #getAvailableThumbSizes()}. Если аргумент
   * и так наименьший допустимый, то возвращает аргумент.
   * <p>
   * Если аргумент - меньше наименьшего допустимого, то возвращает наименьший допустимый размер, который <b>больше</b>
   * аргумента!
   *
   * @param aThumbSize {@link EThumbSize} - исходный размер
   * @return {@link EThumbSize} - меньший допустимый размер, может быть <b>больше</b> аргумента!
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EThumbSize getPrevThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( availableThumbSizes.size() == 1 ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() <= availableThumbSizes.first().size() ) {
      return availableThumbSizes.first();
    }
    if( aThumbSize.size() > availableThumbSizes.last().size() ) {
      return availableThumbSizes.last();
    }
    for( EThumbSize sz = aThumbSize; sz.size() > availableThumbSizes.first().size(); sz = sz.prevSize() ) {
      if( availableThumbSizes.hasElem( sz.prevSize() ) ) {
        return sz.prevSize();
      }
    }
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // May be overriden
  //

  /**
   * Осуществляет сброс масштаба в {@link IThumbSizeable#defaultThumbSize()}.
   * <p>
   * Метод не вызывается, если текущий размер {@link IThumbSizeable#thumbSize()} равен
   * {@link IThumbSizeable#defaultThumbSize()}.
   * <p>
   * В базовом классе устанавливает начальный размер {@link IThumbSizeable#defaultThumbSize()} управляемой сущности.
   * Вызывать ли ролительский метод при переопределении - зависит от логики использования.
   * <p>
   * Внимание: для фактического изменения размера вызывает метод {@link #doSetThumbSize(IThumbSizeable, EThumbSize)}.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность, не бывает <code>null</code>
   */
  public void doSetOriginalSize( IThumbSizeable aSubject ) {
    doSetThumbSize( aSubject, aSubject.defaultThumbSize() );
  }

  /**
   * Осуществляет уменьшение масштаба.
   * <p>
   * Метод не вызывается, если текущий размер {@link IThumbSizeable#thumbSize()} равен {@link EThumbSize#minSize()}.
   * <p>
   * В базовом классе устанавливает предыдущий размер {@link #getPrevThumbSize(EThumbSize)}. Вызывать ли родительский
   * метод при переопределении - зависит от логики использования.
   * <p>
   * Внимание: для фактического изменения размера вызывает метод {@link #doSetThumbSize(IThumbSizeable, EThumbSize)}.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность, не бывает <code>null</code>
   */
  public void doZoomOut( IThumbSizeable aSubject ) {
    doSetThumbSize( aSubject, getPrevThumbSize( subject.thumbSize() ) );
  }

  /**
   * Осуществляет увеличение масштаба.
   * <p>
   * Метод не вызывается, если текущий размер {@link IThumbSizeable#thumbSize()} равен {@link EThumbSize#maxSize()}.
   * <p>
   * В базовом классе устанавливает следующий размер {@link #getNextThumbSize(EThumbSize)}. Вызывать ли родительский
   * метод при переопределении - зависит от логики использования.
   * <p>
   * Внимание: для фактического изменения размера вызывает метод {@link #doSetThumbSize(IThumbSizeable, EThumbSize)}.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность, не бывает <code>null</code>
   */
  public void doZoomIn( IThumbSizeable aSubject ) {
    doSetThumbSize( aSubject, getNextThumbSize( subject.thumbSize() ) );
  }

  /**
   * Изменяет размер минатюр на заданный.
   * <p>
   * В базовом классе просто устанавливает заданный размер aThumbSize. Вызывать ли родительский метод при
   * переопределении - зависит от логики использования.
   * <p>
   * Внимание: этот метод нея/вно вызывается всеми остальными <code>doXxx()</code> методами изменения размеров.
   *
   * @param aSubject {@link IThumbSizeable} - управляемая сущность, не бывает <code>null</code>
   * @param aSize {@link EThumbSize} - новый размер, не бывает <code>null</code>
   */
  public void doSetThumbSize( IThumbSizeable aSubject, EThumbSize aSize ) {
    aSubject.setThumbSize( aSize );
  }

}
