package org.toxsoft.core.tsgui.bricks.tstree;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Column for the tree {@link ITsTreeViewer}.
 *
 * @author hazard157
 */
public interface ITsViewerColumn {

  /**
   * Возвращает текст заголовка этой колонки.
   *
   * @return String - текст заголовка этой колонки
   */
  String title();

  /**
   * Задает текст заголовка этой колонки.
   * <p>
   * Внесенное изменение немедленно отображается в таблице.
   *
   * @param aTitle String - текст заголовка колонки
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setTitle( String aTitle );

  /**
   * Возвращает горизонтальное выравнивание текста внутри колонки.
   * <p>
   * По умолчанию, после создания колонки возвращает выравнивание, подходящее для атомарного типа данного.
   *
   * @return {@link EHorAlignment} - горизонтальное выравнивание текста внутри колонки
   */
  EHorAlignment alignment();

  /**
   * Задает горизонтальное выравнивание текста внутри колонки.
   * <p>
   * Внесенное изменение немедленно отображается в таблице. Выравнивание {@link EHorAlignment#FILL} обрабатывается как
   * {@link EHorAlignment#CENTER}.
   *
   * @param aAlignment {@link EHorAlignment} - горизонтальное выравнивание текста внутри колонки
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setAlignment( EHorAlignment aAlignment );

  /**
   * Возвращает подсказку заголовка этой колонки.
   *
   * @return String - текст заголовка этой колонки
   */
  String tooltip();

  /**
   * Задает подсказку заголовка этой колонки.
   * <p>
   * Внесенное изменение немедленно отображается в просмотрщике.
   *
   * @param aTooltip String - подсказка заголовка колонки
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setTooltip( String aTooltip );

  /**
   * Возвращает значок, отображаемый в заголовке колонки.
   * <p>
   * По умолчанию, после создания колонки возвращает null.
   *
   * @return {@link Image} - значок, отображаемый в заголовке колонки или null
   */
  Image headerImage();

  /**
   * Задает значок, отображаемый в заголовке колонки.
   * <p>
   * Если значок не нужен, следует задать null, что является начальным значением при создании новой колонки.
   * <p>
   * Внесенное изменение немедленно отображается в таблице.
   *
   * @param aImage {@link Image} - значок, отображаемый в заголовке колонки или null
   */
  void setHeaderImage( Image aImage );

  /**
   * Возвращает ширину колонки в пикселях.
   * <p>
   * По умолчанию, после создания колонки ширина устанавливается методом {@link #adjustWidth(String)}, где в качестве
   * строки выступает значение по умолчанию поля, к которому привязана колонка.
   * <p>
   * Для скрытого столбца ({@link #isHidden()} = <code>true</code>) возвращает ширнину, которую будет иметь столбец
   * после показа.
   *
   * @return int - ширина колонки в пикселях
   */
  int width();

  /**
   * Задает ширину колонки в пикселях.
   * <p>
   * Если аргумент aPixelWidth <= 0, то метод ничего не делает.
   * <p>
   * Внесенное изменение немедленно отображается в таблице.
   * <p>
   * Для скрытого столбца ({@link #isHidden()} = <code>true</code>) задает ширнину, которую будет иметь столбец после
   * показа.
   *
   * @param aPixelWidth int - положительная ширина колонки в пикселях
   */
  void setWidth( int aPixelWidth );

  /**
   * Задает ширну колонки на значение по умолчанию.
   * <p>
   * Фактически, вызывает {@link TreeColumn#pack()} или {@link TableColumn#pack()}.
   */
  void pack();

  /**
   * Подбирает ширину колонки, чтобы она вмещала заданную строку с небольшим запасом.
   * <p>
   * Внесенное изменение немедленно отображается в таблице.
   *
   * @param aSampleString String - строка, по которой происходит подбор ширины колонки
   * @throws TsNullArgumentRtException аргумент = null
   */
  void adjustWidth( String aSampleString );

  /**
   * Возвращает признак сокрытия колонки.
   *
   * @return boolean - признак сокрытия колонки<br>
   *         <b>true</b> - в данный момент колонка невидима (скрыта);<br>
   *         <b>false</b> - колонка в данный момент отображается (видима).
   */
  boolean isHidden();

  /**
   * Показывает/скрывает колонку таблицы.
   * <p>
   * При показе ранее скрытой колонки она будет иметь последнюю заданную ширину.
   * <p>
   * Внесенное изменение немедленно отображается в таблице.
   *
   * @param aHidden boolean - признак сокрытия колонки <br>
   *          <b>true</b> - колонка будет скрыта;<br>
   *          <b>false</b> - колонка будет видна.
   */
  void setHidden( boolean aHidden );

  /**
   * Determines if in column cells the thumb (not icon) images will be drawn.
   *
   * @return boolean - <code>true</code> if thumbs will drawn, <code>false</code> - icons
   */
  boolean isUseThumb();

  /**
   * Sets if in column cells the thumb (not icon) images will be drawn.
   *
   * @param aUseThumb boolean - thibs instead of icons flag
   */
  void setUseThumb( boolean aUseThumb );

}
