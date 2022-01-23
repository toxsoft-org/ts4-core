package org.toxsoft.tsgui.bricks.tstree.impl;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.utils.jface.TableLabelProviderAdapter;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Поставщик текста ячеек для дерева узлов {@link ITsNode}.
 * <p>
 * Поставщик ожидает, что все элементы дерева имеют тип {@link ITsNode}. Без перелпределения
 * {@link #doGetColumnText(ITsNode, int)}, для всех столбцов возвращает {@link ITsNode#name()}.
 *
 * @author goga
 */
public class TsTreeLabelProvider
    extends TableLabelProviderAdapter
    implements ITableFontProvider, ITableColorProvider, // эти интерфейсы для таблицы
    IColorProvider, IFontProvider // а эти - для дерева
{

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
   * Размер запрашиваемых у узлов значков.
   */
  private EIconSize iconSize = EIconSize.IS_16X16;

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
    if( aElement instanceof ITsNode ) {
      ITsNode node = (ITsNode)aElement;
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
    if( aElement instanceof ITsNode ) {
      ITsNode node = (ITsNode)aElement;
      Image img = doGetColumnImage( node, aColumnIndex, iconSize );
      if( img != null ) {
        return img;
      }
      if( aColumnIndex == 0 ) { // для первого столбца вернем изображение элемента, если есть
        return node.getImage( iconSize );
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
  // Реализация интерфейса ITableColorProvider
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
  // Реализация интерфейса IColorProvider
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
  // Реализация интерфейса ITableFontProvider
  //

  @Override
  public Font getFont( Object aElement, int aColumnIndex ) {
    if( fontProvider != null ) {
      return fontProvider.getFont( aElement, aColumnIndex );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFontProvider
  //

  @Override
  public Font getFont( Object aElement ) {
    return getFont( aElement, 0 );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает размер запрашиваемых у узлов значков.
   *
   * @return {@link EIconSize} - размер запрашиваемых у узлов значков
   */
  public EIconSize iconSize() {
    return iconSize;
  }

  /**
   * Задает размер запрашиваемых у узлов значков.
   * <p>
   * Метод не приводит к обновлнеию чего-либо.
   *
   * @param aIconSize {@link EIconSize} - размер запрашиваемых у узлов значков
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    iconSize = aIconSize;
  }

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
