package org.toxsoft.core.singlesrc;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательные методы для сокрытия различия реализации RCP и RAP.
 * <p>
 * Существует две реализации этого класса - для RCP и RAP отдельно. Интерфейс у нх одинаковый.
 * <p>
 * Для использование надо в раздел Imported Packages использующего плагина указать этот пакет, и <b>не следует</b>
 * указывать этот плагин в зависимостьях (Required Plug-ins). Надо также проследить, чтоы в Run Configuration запуска
 * RCP/RAP приложения входили соответственно RCP/RAP версии этого плагина.
 * <p>
 *
 * @author hazard157
 */
public class TsSinglesourcingUtils {

  // ------------------------------------------------------------------------------------
  /**
   * Эта часть класса одинакова и для RAP и для RCP версии.
   * <p>
   * Делаем copy-paste только для того, чтобы для временного класса не заводить отдельный проект, содержащий общий код.
   */

  /**
   * Признак RAP приложения, изменяется в методе {@link #setIsRcpApplication()}.
   */
  private static boolean isRapApplication = false;

  /**
   * Возвращает признак, что приложение работает в RAP
   *
   * @return boolean - признак RAP приложения<br>
   *         <b>true</b> - это RAP приложение;<br>
   *         <b>false</b> - это RCP приложение (других вариантов пока нет).
   */
  public static boolean isRap() {
    return isRapApplication;
  }

  /**
   * Изменяет значение признака {@link #isRap()} на <code>false</code>.
   * <p>
   * Значение признака {@link #isRap()} по умолчанию <code>true</code>, и только для RCP приложении надо вызвать этот
   * метод.
   * <p>
   * <b>Внимание:</b> этот метод должен быть вызван единственный раз, первой же строкой в классе-стартере RCP
   * приложения. Для E4 приложении должен быть первой строкой в первом addone.
   */
  public static void setIsRcpApplication() {
    isRapApplication = false;
  }

  /**
   * Запрет на создание экземпляров.
   */
  private TsSinglesourcingUtils() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  /**
   * Эта часть класса - разные реализации для разны целевых платформ.
   * <p>
   * Ниже - реализация для RCP.
   */

  /**
   * Создает курсор конструктором {@link Cursor#Cursor(Device, ImageData, int, int)}.
   *
   * @param aDevice {@link Device} - the device on which to allocate the cursor
   * @param aSource {@link ImageData} - the image data for the cursor
   * @param aHotspotX int - the x coordinate of the cursor's hotspot
   * @param aHotspotY int - the y coordinate of the cursor's hotspot
   * @return {@link Cursor} - созданный экземпляр
   */
  public static Cursor Cursor_Cursor( Device aDevice, ImageData aSource, int aHotspotX, int aHotspotY ) {
    return new Cursor( aDevice, aSource, aHotspotX, aHotspotY );
  }

  /**
   * Выводит диалог выбора файла и возвращает значение с диалога.
   * <p>
   * В этой реализации RCP все так и происходит.
   *
   * @param aShell {@link Shell} - родительский метод
   * @param aSwtStyle int - SWT стили создания диалога
   * @param aExtensions String[] - расширения файло
   * @param aFileName String - начальное имя файла
   * @return String - результат выполнения {@link FileDialog#open()}
   */
  public static String FileDialog_open( Shell aShell, int aSwtStyle, String[] aExtensions, String aFileName ) {
    FileDialog fileDialog = new FileDialog( aShell, aSwtStyle );
    fileDialog.setFileName( aFileName );
    fileDialog.setFilterExtensions( aExtensions );
    return fileDialog.open();
  }

  /**
   * Аналог aText.getCaretPosition().
   *
   * @param aText {@link Text} текст
   * @return int текущая позиция
   */
  public static int Text_getCaretPosition( Text aText ) {
    return aText.getCaretLocation().x;
  }

  /**
   * Аналог MenuItem.setToolTipText(String).
   *
   * @param aMenuItem {@link MenuItem} - элемент меню
   * @param aTooltip String - текст подсказки
   */
  public static void MenuItem_setToolTipText( MenuItem aMenuItem, String aTooltip ) {
    aMenuItem.setToolTipText( aTooltip );
  }

  /**
   * Аналог ScrollBar.getIncrement().
   *
   * @param aScrollBar {@link ScrollBar} - скроллер
   * @return int - значение приращени шага
   */
  public static int ScrollBar_getIncrement( ScrollBar aScrollBar ) {
    return aScrollBar.getIncrement();
  }

  /**
   * Аналог ScrollBar.getPageIncrement().
   *
   * @param aScrollBar {@link ScrollBar} - скроллер
   * @return int - значение прыжка
   */
  public static int ScrollBar_getPageIncrement( ScrollBar aScrollBar ) {
    return aScrollBar.getPageIncrement();
  }

  /**
   * Задает тип (сплошной/разные пунктиры) линии, не работает в RAP.
   *
   * @param aGc {@link GC} - графический контекст
   * @param aCustom boolean - признак пользовтельского типа (иначе будет <code>SWT.LINE_SOLID</code>)
   * @param aDashes int[] - при пользовательском типе задает чередование в пунктире (для сплошной линии может быть null)
   */
  public static void GC_setLineType( GC aGc, boolean aCustom, int[] aDashes ) {
    if( aCustom ) {
      aGc.setLineStyle( SWT.LINE_CUSTOM );
      aGc.setLineDash( aDashes );
    }
    else {
      aGc.setLineStyle( SWT.LINE_SOLID );
    }
  }

  /**
   * Задает тип (сплошной/разные пунктиры) линии, не работает в RAP.
   *
   * @param aGc {@link GC} - графический контекст
   * @param aLineStyle int - тип линии, один из <code>SWT.LINE_XXX</code>
   */
  public static void GC_setLineStyle( GC aGc, int aLineStyle ) {
    aGc.setLineStyle( aLineStyle );
  }

  /**
   * Аналог Path.getBounds(float).
   *
   * @param aPath {@link Path} - кривая
   * @param aBounds float[] - массив для чтерыех координат прямоугольника
   */
  public static void Path_getBounds( Path aPath, float[] aBounds ) {
    aPath.getBounds( aBounds );
  }

  /**
   * Аналог RCP конструктора LineAttributes.LineAttributes(float width, int cap, int join, int style, float[] dash,
   * float dashOffset, float miterLimit).
   * <p>
   * Create a new line attributes with the specified arguments.
   *
   * @param width the line width
   * @param cap the line cap style
   * @param join the line join style
   * @param style the line style
   * @param dash the line dash style
   * @param dashOffset the line dash style offset
   * @param miterLimit the line miter limit
   * @return {@link LineAttributes} - соданные атрибуты линии
   */
  public static LineAttributes LineAttributes_LineAttributes( float width, int cap, int join, int style, float[] dash,
      float dashOffset, float miterLimit ) {
    return new LineAttributes( width, cap, join, style, dash, dashOffset, miterLimit );
  }

  /**
   * Аналог Control.addMouseMoveListener(MouseMoveListener).
   * <p>
   * Adds the listener to the collection of listeners who will be notified when the mouse moves, by sending it one of
   * the messages defined in the <code>MouseMoveListener</code> interface.
   *
   * @param aControl control to add listener to
   * @param aMouseMoveListener the listener which should be notified
   */
  public static void Control_addMouseMoveListener( Control aControl, MouseMoveListener aMouseMoveListener ) {
    aControl.addMouseMoveListener( aMouseMoveListener );
  }

  /**
   * Аналог Control.removeMouseMoveListener(MouseMoveListener).
   * <p>
   * Removes the listener from the collection of listeners who will be notified when the mouse moves.
   *
   * @param aControl control to remove listener from
   * @param aMouseMoveListener the listener which should no longer be notified
   */
  public static void Control_removeMouseMoveListener( Control aControl, MouseMoveListener aMouseMoveListener ) {
    aControl.removeMouseMoveListener( aMouseMoveListener );
  }

  private static final IMapEdit<ISingleSourcing_MouseWheelListener, MouseWheelListener> mwlMap = new ElemMap<>();

  /**
   * Аналог <code>Control.addMouseWheelListener(MouseMoveListener)</code>.
   * <p>
   * Вместо отсутствующего в RAP <code>MouseMoveListener</code> используется {@link ISingleSourcing_MouseWheelListener}.
   *
   * @param aControl {@link Control} - контроль
   * @param aListener {@link ISingleSourcing_MouseWheelListener} - слушатель
   */
  public static void Control_addMouseWheelListener( Control aControl, ISingleSourcing_MouseWheelListener aListener ) {
    MouseWheelListener mwl;
    synchronized (mwlMap) {
      mwl = mwlMap.findByKey( aListener );
      if( mwl == null ) {
        mwl = aEvent -> aListener.mouseScrolled( aEvent );
        mwlMap.put( aListener, mwl );
      }
    }
    aControl.addMouseWheelListener( mwl );
    // GOGA 2023-12-07 - turn off built-in mouse wheel handling
    // aControl.addListener( SWT.MouseVerticalWheel, aEvent -> aEvent.doit = false );
  }

  /**
   * Аналог <code>Control.addMouseWheelListener(MouseMoveListener)</code>.
   * <p>
   * Вместо отсутствующего в RAP <code>MouseMoveListener</code> используется {@link ISingleSourcing_MouseWheelListener}.
   *
   * @param aControl {@link Control} - контроль
   * @param aListener {@link ISingleSourcing_MouseWheelListener} - слушатель
   */
  public static void Control_removeMouseWheelListener( Control aControl,
      ISingleSourcing_MouseWheelListener aListener ) {
    MouseWheelListener mwl;
    synchronized (mwlMap) {
      mwl = mwlMap.findByKey( aListener );
    }
    if( mwl != null ) {
      aControl.removeMouseWheelListener( mwl );
    }
  }

  private static final IMapEdit<ISingleSourcing_MouseTrackListener, MouseTrackListener> mtlMap = new ElemMap<>();

  /**
   * Аналог <code>Control.addMouseTrackListener(MouseTrackListener)</code>.
   * <p>
   * Вместо отсутствующего в RAP <code>MouseTrackListener</code> используется
   * {@link ISingleSourcing_MouseTrackListener}.
   *
   * @param aControl {@link Control} - контроль
   * @param aListener {@link ISingleSourcing_MouseTrackListener} - слушатель
   */
  public static void Control_addMouseTrackListener( Control aControl, ISingleSourcing_MouseTrackListener aListener ) {
    MouseTrackListener mtl;
    synchronized (mtlMap) {
      mtl = mtlMap.findByKey( aListener );
      if( mtl == null ) {
        mtl = new MouseTrackListener() {

          @Override
          public void mouseEnter( MouseEvent aEvent ) {
            aListener.mouseEnter( aEvent );
          }

          @Override
          public void mouseExit( MouseEvent aEvent ) {
            aListener.mouseExit( aEvent );
          }

          @Override
          public void mouseHover( MouseEvent aEvent ) {
            aListener.mouseHover( aEvent );
          }

        };
        mtlMap.put( aListener, mtl );
      }
    }
    aControl.addMouseTrackListener( mtl );
  }

  /**
   * Аналог SashForm.setWeights(int,int).
   *
   * @param aForm {@link SashForm} - форма
   * @param aWeight1 int размер первой панели
   * @param aWeight2 int размер первой панели
   */
  public static void SashForm_setWeights( SashForm aForm, int aWeight1, int aWeight2 ) {
    aForm.setWeights( aWeight1, aWeight2 );
  }

  /**
   * Аналог <code>Control.addMouseTrackListener(MouseMoveListener)</code>.
   * <p>
   * Вместо отсутствующего в RAP <code>MouseTrackListener</code> используется
   * {@link ISingleSourcing_MouseTrackListener}.
   *
   * @param aControl {@link Control} - контроль
   * @param aListener {@link ISingleSourcing_MouseTrackListener} - слушатель
   */
  public static void Control_removeMouseTrackListener( Control aControl,
      ISingleSourcing_MouseTrackListener aListener ) {
    MouseTrackListener mtl;
    synchronized (mtlMap) {
      mtl = mtlMap.findByKey( aListener );
    }
    if( mtl != null ) {
      aControl.removeMouseTrackListener( mtl );
    }
  }

  // 2021-05-18 mvk
  /**
   * Возвращает состояние режима принудительной перерисовки {@link Canvas} для RAP
   * <p>
   * Смотри реализацию CanvasLCA в rap-toxsoft-форк с версии 3.16 и выше.
   * <p>
   * Значение по умолчанию: принудительная перерисовка запрещена.
   * <p>
   * После выполнения цикла перерисовки {@link Canvas} устанавливается значение по умолчанию (false).
   * <p>
   * Для RCP всегда false
   *
   * @param aCanvas {@link Canvas}
   * @return boolean текущее состояние режима
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean Canvas_getForceRedraw( Canvas aCanvas ) {
    TsNullArgumentRtException.checkNull( aCanvas );
    return false;
  }

  /**
   * Устанавливает режим принудительной перерисовки {@link Canvas} для RAP
   * <p>
   * Смотри реализацию CanvasLCA в rap-toxsoft-форк с версии 3.16 и выше.
   * <p>
   * Значение по умолчанию: принудительная перерисовка запрещена.
   * <p>
   * После выполнения цикла перерисовки {@link Canvas} устанавливается значение по умолчанию (false).
   * <p>
   * Для RCP ничего не делает и всегда возвращает false
   *
   * @param aCanvas {@link Canvas}
   * @param aForce boolean <b>true</b> разрешить инициализацию отрисовки {@link Canvas};<b>false</b> запретить
   *          инициализацию {@link Canvas}
   * @return boolean значение до установки
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean Canvas_setForceRedraw( Canvas aCanvas, boolean aForce ) {
    TsNullArgumentRtException.checkNull( aCanvas );
    return false;
  }
}
