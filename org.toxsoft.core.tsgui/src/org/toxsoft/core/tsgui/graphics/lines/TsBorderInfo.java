package org.toxsoft.core.tsgui.graphics.lines;

import static org.toxsoft.core.tsgui.graphics.lines.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * All attributes to draw rectangular border.
 * <p>
 *
 * @author vs
 */
public final class TsBorderInfo {

  static final IDataDef OPDEF_SINGLE = DataDef.create( "single", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_SINGLE, //
      TSID_DESCRIPTION, STR_D_SINGLE, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_LEFT_RGBA = DataDef.create( "leftTopRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_LEFT_RGBA, //
      TSID_DESCRIPTION, STR_D_LEFT_RGBA, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  static final IDataDef OPDEF_RIGHT_RGBA = DataDef.create( "rightBottomRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_RIGHT_RGBA, //
      TSID_DESCRIPTION, STR_D_RIGHT_RGBA, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  static final IDataDef OPDEF_LINE_INFO = DataDef.create( "lineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.DEFAULT ) //
  );

  static final IDataDef OPDEF_PAINT_LEFT = DataDef.create( "paintLeft", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_PAINT_LEFT, //
      TSID_DESCRIPTION, STR_D_PAINT_LEFT, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_PAINT_TOP = DataDef.create( "paintTop", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_PAINT_TOP, //
      TSID_DESCRIPTION, STR_D_PAINT_TOP, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_PAINT_RIGHT = DataDef.create( "paintRight", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_PAINT_RIGHT, //
      TSID_DESCRIPTION, STR_D_PAINT_RIGHT, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_PAINT_BOTTOM = DataDef.create( "paintBottom", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_PAINT_BOTTOM, //
      TSID_DESCRIPTION, STR_D_PAINT_BOTTOM, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  /**
   * Все описания опций
   */
  public static IStridablesList<IDataDef> ALL_DEFS = new StridablesList<>( //
      OPDEF_SINGLE, //
      OPDEF_LEFT_RGBA, //
      OPDEF_RIGHT_RGBA, //
      OPDEF_LINE_INFO, //
      OPDEF_PAINT_LEFT, //
      OPDEF_PAINT_TOP, //
      OPDEF_PAINT_RIGHT, //
      OPDEF_PAINT_BOTTOM //
  );

  /**
   * Default simple rectagular border of 1 pixel width.
   */
  public static final TsBorderInfo DEFAULT = new TsBorderInfo( OptionSetUtils.createOpSet( //
      OPDEF_LINE_INFO, TsLineInfo.fromLineAttributes( new LineAttributes( 1, SWT.CAP_FLAT, SWT.JOIN_MITER ) ), //
      OPDEF_LEFT_RGBA, new RGBA( 0, 0, 0, 255 ), //
      OPDEF_RIGHT_RGBA, new RGBA( 0, 0, 0, 255 ) //
  ) );

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsBorderInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsBorderInfo> KEEPER =
      new AbstractEntityKeeper<>( TsBorderInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, DEFAULT ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsBorderInfo aEntity ) {
          OptionSetKeeper.KEEPER.write( aSw, aEntity.options );
        }

        @Override
        protected TsBorderInfo doRead( IStrioReader aSr ) {
          IOptionSet opSet = OptionSetKeeper.KEEPER.read( aSr );
          return new TsBorderInfo( opSet );
          // return new TsBorderInfo( single, lineAttrs, rgbaLeft, rgbaRight, paintFlags );
        }

      };

  IOptionSetEdit options = new OptionSet();

  /**
   * Создает границу в виде одноцветного прямоугольника.
   *
   * @param aWidth int - толщина линии
   * @param aRgba RGBA - цвет границы
   * @return TsBorderInfo - описание границы
   */
  public static TsBorderInfo createSimpleBorder( int aWidth, RGBA aRgba ) {
    IOptionSetEdit opSet = new OptionSet();
    LineAttributes lineAttrs = new LineAttributes( aWidth, SWT.CAP_FLAT, SWT.JOIN_MITER );
    opSet.setValobj( OPDEF_LINE_INFO, lineAttrs );
    return new TsBorderInfo( opSet );
  }

  /**
   * Создает одинарную границу в виде двух-цветного прямоугольника.<br>
   *
   * @param aWidth int - толщина линии
   * @param aLeftTopRgba RGBA - параметры цвета линий, которые расположены левее и выше
   * @param aRightBottomRgba RGBA - параметры цвета линий, которые расположены правее и ниже
   * @return TsBorderInfo - описание границы
   */
  public static TsBorderInfo createSingleBorder( int aWidth, RGBA aLeftTopRgba, RGBA aRightBottomRgba ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValobj( OPDEF_LEFT_RGBA, aLeftTopRgba );
    opSet.setValobj( OPDEF_RIGHT_RGBA, aRightBottomRgba );
    LineAttributes lineAttrs = new LineAttributes( aWidth, SWT.CAP_FLAT, SWT.JOIN_MITER );
    opSet.setValobj( OPDEF_LINE_INFO, lineAttrs );
    return new TsBorderInfo( opSet );
  }

  /**
   * Создает двойную границу в виде двух одноцветных прямоугольников.<br>
   *
   * @param aWidth int - толщина линии
   * @param aLeftTopRgba RGBA - параметры цвета линий, которые расположены левее и выше
   * @param aRightBottomRgba RGBA - параметры цвета линий, которые расположены правее и ниже
   * @return TsBorderInfo - описание границы
   */
  public static TsBorderInfo createDoubleBorder( int aWidth, RGBA aLeftTopRgba, RGBA aRightBottomRgba ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setBool( OPDEF_SINGLE, false );
    opSet.setValobj( OPDEF_LEFT_RGBA, aLeftTopRgba );
    opSet.setValobj( OPDEF_RIGHT_RGBA, aRightBottomRgba );
    LineAttributes lineAttrs = new LineAttributes( aWidth, SWT.CAP_FLAT, SWT.JOIN_MITER );
    opSet.setValobj( OPDEF_LINE_INFO, lineAttrs );
    return new TsBorderInfo( opSet );
  }

  /**
   * Конструктор.<br>
   *
   * @param aOpSet IOptionSet - свойства границы
   */
  public TsBorderInfo( IOptionSet aOpSet ) {
    options.setAll( aOpSet );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает свойства границы в виде {@link IOptionSet}.
   *
   * @return IOptionSet - свойства границы
   */
  public IOptionSet options() {
    return options;
  }

  /**
   * Вовзращает признак того, является ли граница одинарной.<br>
   *
   * @return <b>true</b> - одинарная граница<br>
   *         <b>false</b> - двойная граница
   */
  public boolean isSingle() {
    return options.getBool( OPDEF_SINGLE );
  }

  /**
   * Возвращает параметры цвета для левой и верхней линии границы.<br>
   *
   * @return RGBA - параметры цвета для левой и верхней линии границы
   */
  public RGBA leftTopRGBA() {
    return options.getValobj( OPDEF_LEFT_RGBA );
  }

  /**
   * Возвращает параметры цвета для правой и нижней линии границы.<br>
   *
   * @return RGBA - параметры цвета для правой и нижней линии границы
   */
  public RGBA rightBottomRGBA() {
    return options.getValobj( OPDEF_RIGHT_RGBA );
  }

  /**
   * returns the line width.
   *
   * @return int - the line width in pixels
   */
  public int width() {
    TsLineInfo lineInfo = options.getValobj( OPDEF_LINE_INFO );
    return lineInfo.width();
  }

  /**
   * Returns the line info.
   *
   * @return TsLineInfo - lineInfo
   */
  public TsLineInfo lineInfo() {
    return options.getValobj( OPDEF_LINE_INFO );
  }

  /**
   * Возвращет признак того, нужно ли рисовать левую сторону границы.
   *
   * @return <b>true</b> - нужно рисовать<br>
   *         <b>false</b> - рисовать не нужно
   */
  public boolean shouldPaintLeft() {
    return options.getBool( OPDEF_PAINT_LEFT );
  }

  /**
   * Возвращет признак того, нужно ли рисовать верхнюю сторону границы.
   *
   * @return <b>true</b> - нужно рисовать<br>
   *         <b>false</b> - рисовать не нужно
   */
  public boolean shouldPaintTop() {
    return options.getBool( OPDEF_PAINT_TOP );
  }

  /**
   * Возвращет признак того, нужно ли рисовать правую сторону границы.
   *
   * @return <b>true</b> - нужно рисовать<br>
   *         <b>false</b> - рисовать не нужно
   */
  public boolean shouldPaintRight() {
    return options.getBool( OPDEF_PAINT_RIGHT );
  }

  /**
   * Возвращет признак того, нужно ли рисовать нижнюю сторону границы.
   *
   * @return <b>true</b> - нужно рисовать<br>
   *         <b>false</b> - рисовать не нужно
   */
  public boolean shouldPaintBottom() {
    return options.getBool( OPDEF_PAINT_BOTTOM );
  }

  /**
   * Возвращет признак того, нужно ли рисовать все стороны границы.
   *
   * @return <b>true</b> - нужно рисовать все стороны<br>
   *         <b>false</b> - рисовать нужно не все стороны
   */
  public boolean shouldPaintAll() {
    return shouldPaintLeft() && shouldPaintTop() && shouldPaintRight() && shouldPaintBottom();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return options.toString();
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj instanceof IOptionSet that ) {
      return options.equals( that );
    }
    return false;
  }

  @Override
  public int hashCode() {
    return options.hashCode();
  }

}
