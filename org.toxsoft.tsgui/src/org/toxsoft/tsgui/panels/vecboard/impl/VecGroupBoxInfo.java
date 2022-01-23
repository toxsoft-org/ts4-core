package org.toxsoft.tsgui.panels.vecboard.impl;

import org.toxsoft.tsgui.graphics.EBorderType;
import org.toxsoft.tsgui.panels.vecboard.IVecGroupBoxInfo;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Неизменяемая реализация {@link IVecGroupBoxInfo}.
 *
 * @author goga
 */
public class VecGroupBoxInfo
    implements IVecGroupBoxInfo {

  static class Keeper
      extends AbstractEntityKeeper<IVecGroupBoxInfo> {

    protected Keeper() {
      super( IVecGroupBoxInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
    }

    @Override
    protected void doWrite( IStrioWriter aSw, IVecGroupBoxInfo aEntity ) {
      aSw.writeQuotedString( aEntity.title() );
      aSw.writeSeparatorChar();
      aSw.writeQuotedString( aEntity.tooltipText() );
      aSw.writeSeparatorChar();
      aSw.writeAsIs( aEntity.borderType().id() );
    }

    @Override
    protected IVecGroupBoxInfo doRead( IStrioReader aSr ) {
      String title = aSr.readQuotedString();
      aSr.ensureSeparatorChar();
      String tooltipText = aSr.readQuotedString();
      aSr.ensureSeparatorChar();
      EBorderType borderType = EBorderType.findById( aSr.readIdPath() );
      return new VecGroupBoxInfo( title, tooltipText, borderType );
    }

  }

  /**
   * Хранитель сущности в текстовое представление.
   */
  public static final IEntityKeeper<IVecGroupBoxInfo> KEEPER = new Keeper();

  private final String      title;
  private final String      tooltipText;
  private final EBorderType borderType;

  /**
   * Создает объект со всеми инвариантами.
   *
   * @param aTitle String - заголовок
   * @param aTooltipText String - всплывающая подсказка
   * @param aBorderType {@link EBorderType} - тип границы
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public VecGroupBoxInfo( String aTitle, String aTooltipText, EBorderType aBorderType ) {
    TsNullArgumentRtException.checkNulls( aTitle, aTooltipText, aBorderType );
    title = aTitle;
    tooltipText = aTooltipText;
    borderType = aBorderType;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGroupBoxInfo
  //

  @Override
  public String title() {
    return title;
  }

  @Override
  public String tooltipText() {
    return tooltipText;
  }

  @Override
  public EBorderType borderType() {
    return borderType;
  }

}
