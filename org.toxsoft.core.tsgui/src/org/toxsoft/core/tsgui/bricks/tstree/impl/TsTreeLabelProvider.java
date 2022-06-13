package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Поставщик текста ячеек для дерева узлов {@link ITsNode}.
 * <p>
 * Поставщик ожидает, что все элементы дерева имеют тип {@link ITsNode}. Без перелпределения
 * {@link #doGetColumnText(ITsNode, int)}, для всех столбцов возвращает {@link ITsNode#name()}.
 *
 * @author hazard157
 */
public class TsTreeLabelProvider
    extends TableLabelProviderAdapter
    implements ITableFontProvider, ITableColorProvider, // эти интерфейсы для таблицы
    IColorProvider, IFontProvider, // а эти - для дерева
    IIconSizeable, IThumbSizeable //
{

  private static final EIconSize  DEFAULT_ICON_SIZE  = EIconSize.IS_16X16;
  private static final EThumbSize DEFAULT_THUMB_SIZE = EThumbSize.SZ64;

  /**
   * Поставщик шрифта для отображения ячейки таблицы.
   * <p>
   * Задается пользователем в {@link #setFontProvider(ITableFontProvider)}, null означает использование значений по
   * умолчанию.
   */
  ITableFontProvider  fontProvider  = null;
  /**
   * Поставщик цветов для отображения ячейки таблицы.
   * <p>
   * Задается пользователем в {@link #setColorProvider(ITableColorProvider)}, null означает использование значений по
   * умолчанию.
   */
  ITableColorProvider colorProvider = null;

  /**
   * Icon size asked to the cells.
   */
  private EIconSize iconSize = DEFAULT_ICON_SIZE;

  /**
   * Thuimb size asked to the cells.
   */
  private EThumbSize thumbSize = DEFAULT_THUMB_SIZE;

  /**
   * Пустой конструктор.
   */
  public TsTreeLabelProvider() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса TableLabelProviderAdapter
  //

  @Override
  public String getColumnText( Object aElement, int aColumnIndex ) {
    if( aElement instanceof ITsNode node ) {
      String s = doGetColumnText( node, aColumnIndex );
      if( s != null ) {
        return s;
      }
      return node.name();
    }
    return null;
  }

  @Override
  public String getText( Object aElement ) {
    return getColumnText( aElement, 0 );
  }

  @Override
  public Image getColumnImage( Object aElement, int aColumnIndex ) {
    if( aElement instanceof ITsNode node ) {
      Image img = doGetColumnImage( node, aColumnIndex, iconSize );
      if( img != null ) {
        return img;
      }
      if( aColumnIndex == 0 ) { // для первого столбца вернем изображение элемента, если есть
        return node.getIcon( iconSize );
      }
    }
    return null;
  }

  @Override
  public Image getImage( Object aElement ) {
    return getColumnImage( aElement, 0 );
  }

  // ------------------------------------------------------------------------------------
  // Методы для реализации наследниками
  //

  /**
   * Наследники могут переопределить метод для возврата разных строк для разных столбцов.
   * <p>
   * В базвовом классе просто возвращает null, при переопределении метод базового класса вызывать не нужно.
   *
   * @param aNode {@link ITsNode} - запрашиваемый узел, не бывает null
   * @param aColumnIndex int - индекс столбца (0 испольхзуется для вьюеров без столбцов)
   * @return String - отображаемая строка или null для использования отображения по умолчанию
   */
  protected String doGetColumnText( ITsNode aNode, int aColumnIndex ) {
    return null;
  }

  /**
   * Наследники могут переопределить метод для возврата разных значков для разных столбцов.
   * <p>
   * В базвовом классе просто возвращает null, при переопределении метод базового класса вызывать не нужно.
   *
   * @param aNode {@link ITsNode} - запрашиваемый узел, не бывает null
   * @param aColumnIndex int - индекс столбца (0 испольхзуется для вьюеров без столбцов)
   * @param aIconSize {@link EIconSize} - рекомендуемый размер значка
   * @return Image - значок или null для использования отображения по умолчанию
   */
  protected Image doGetColumnImage( ITsNode aNode, int aColumnIndex, EIconSize aIconSize ) {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // ITableColorProvider
  //

  @Override
  public Color getForeground( Object aElement, int aColumnIndex ) {
    if( colorProvider != null ) {
      return colorProvider.getForeground( aElement, aColumnIndex );
    }
    return null;
  }

  @Override
  public Color getBackground( Object aElement, int aColumnIndex ) {
    if( colorProvider != null ) {
      return colorProvider.getBackground( aElement, aColumnIndex );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IColorProvider
  //

  @Override
  public Color getForeground( Object aElement ) {
    return getForeground( aElement, 0 );
  }

  @Override
  public Color getBackground( Object aElement ) {
    return getBackground( aElement, 0 );
  }

  // ------------------------------------------------------------------------------------
  // ITableFontProvider
  //

  @Override
  public Font getFont( Object aElement, int aColumnIndex ) {
    if( fontProvider != null ) {
      return fontProvider.getFont( aElement, aColumnIndex );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IFontProvider
  //

  @Override
  public Font getFont( Object aElement ) {
    return getFont( aElement, 0 );
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeable
  //

  @Override
  public EIconSize iconSize() {
    return iconSize;
  }

  @Override
  public EIconSize defaultIconSize() {
    return DEFAULT_ICON_SIZE;
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    iconSize = aIconSize;
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeable
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return DEFAULT_THUMB_SIZE;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    thumbSize = aThumbSize;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает поставщика шрифта для ячеек таблицы или null, если используются только шрифты по умолчанию.
   * <p>
   * Внимание: после этого метода следует обновить отрисовщики ячеек вызовом
   * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
   *
   * @param aFontProvider {@link ITableFontProvider} - поставщик шрифтов ячеек или null
   */
  public void setFontProvider( ITableFontProvider aFontProvider ) {
    fontProvider = aFontProvider;
  }

  /**
   * Задает поставщика цветов для ячеек таблицы или null, если используются только цвета по умолчанию.
   * <p>
   * Внимание: после этого метода следует обновить отрисовщики ячеек вызовом
   * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
   *
   * @param aColorProvider {@link ITableColorProvider} - поставщик цветов ячеек или null
   */
  public void setColorProvider( ITableColorProvider aColorProvider ) {
    colorProvider = aColorProvider;
  }

}
