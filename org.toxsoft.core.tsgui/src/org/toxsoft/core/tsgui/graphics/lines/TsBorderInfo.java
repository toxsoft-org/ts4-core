package org.toxsoft.core.tsgui.graphics.lines;

import static org.toxsoft.core.tsgui.graphics.lines.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * All options determining how to draw rectangular border.
 *
 * @author vs
 */
public final class TsBorderInfo {

  static final IDataDef OPDEF_BORDER_KIND = DataDef.create( "kind", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_KIND, //
      TSID_DESCRIPTION, STR_D_BI_KIND, //
      TSID_KEEPER_ID, ETsBorderKind.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsBorderKind.SINGLE ) //
  );

  static final IDataDef OPDEF_LEFT_RGBA = DataDef.create( "leftTopRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_LEFT_RGBA, //
      TSID_DESCRIPTION, STR_D_BI_LEFT_RGBA, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  static final IDataDef OPDEF_RIGHT_RGBA = DataDef.create( "rightBottomRgba", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_RIGHT_RGBA, //
      TSID_DESCRIPTION, STR_D_BI_RIGHT_RGBA, //
      TSID_KEEPER_ID, RGBAKeeper.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSimpleRgba.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RGBA( 0, 0, 0, 255 ) ) //
  );

  static final IDataDef OPDEF_LINE_INFO = DataDef.create( "lineInfo", VALOBJ, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_LINE_INFO, //
      TSID_DESCRIPTION, STR_D_BI_LINE_INFO, //
      TSID_KEEPER_ID, TsLineInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjTsLineInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( TsLineInfo.DEFAULT ) //
  );

  static final IDataDef OPDEF_PAINT_LEFT = DataDef.create( "paintLeft", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_PAINT_LEFT, //
      TSID_DESCRIPTION, STR_D_BI_PAINT_LEFT, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_PAINT_TOP = DataDef.create( "paintTop", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_PAINT_TOP, //
      TSID_DESCRIPTION, STR_D_BI_PAINT_TOP, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_PAINT_RIGHT = DataDef.create( "paintRight", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_PAINT_RIGHT, //
      TSID_DESCRIPTION, STR_D_BI_PAINT_RIGHT, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  static final IDataDef OPDEF_PAINT_BOTTOM = DataDef.create( "paintBottom", BOOLEAN, // //$NON-NLS-1$
      TSID_NAME, STR_N_BI_PAINT_BOTTOM, //
      TSID_DESCRIPTION, STR_D_BI_PAINT_BOTTOM, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  /**
   * All options as one list.
   */
  public static IStridablesList<IDataDef> ALL_DEFS = new StridablesList<>( //
      OPDEF_BORDER_KIND, //
      OPDEF_LEFT_RGBA, //
      OPDEF_RIGHT_RGBA, //
      OPDEF_LINE_INFO, //
      OPDEF_PAINT_LEFT, //
      OPDEF_PAINT_TOP, //
      OPDEF_PAINT_RIGHT, //
      OPDEF_PAINT_BOTTOM //
  );

  /**
   * Singleton of the no border (border of kind {@link ETsBorderKind#NONE}).
   */
  public static final TsBorderInfo NONE = new TsBorderInfo( OptionSetUtils.createOpSet( //
      OPDEF_BORDER_KIND, avValobj( ETsBorderKind.NONE ) ) //
  );

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsBorderInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsBorderInfo> KEEPER =
      new AbstractEntityKeeper<>( TsBorderInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsBorderInfo aEntity ) {
          OptionSetKeeper.KEEPER.write( aSw, aEntity.options );
        }

        @Override
        protected TsBorderInfo doRead( IStrioReader aSr ) {
          IOptionSet opSet = OptionSetKeeper.KEEPER.read( aSr );
          return new TsBorderInfo( opSet );
        }

      };

  private final IOptionSet options;

  /**
   * Private constructor.
   * <p>
   * Use static constructors or {@link #NONE} constant for instance creation.
   * <p>
   * Note on implementation: constructor stores reference to the argument without creating defensive copy.
   *
   * @param aOpSet {@link IOptionSet} - the option values making the border info
   */
  private TsBorderInfo( IOptionSet aOpSet ) {
    options = aOpSet;
  }

  /**
   * Creates instance from options.
   * <p>
   * Only options listed in {@link #ALL_DEFS} will remain in the internal options set.
   *
   * @param aOptions {@link IOptionSet} - border options
   * @return {@link TsBorderInfo} - created instance
   */
  public static TsBorderInfo ofOptions( IOptionSet aOptions ) {
    TsNullArgumentRtException.checkNull( aOptions );
    IOptionSetEdit ops = new OptionSet();
    for( String opId : aOptions.keys() ) {
      if( ALL_DEFS.hasKey( opId ) ) {
        ops.setValue( opId, aOptions.getByKey( opId ) );
      }
    }
    return new TsBorderInfo( ops );
  }

  /**
   * Simply Returns {@link #NONE}.
   *
   * @return {@link TsBorderInfo} - no border info
   */
  public static TsBorderInfo ofNone() {
    return NONE;
  }

  /**
   * Creates border info of kind {@link ETsBorderKind#SINGLE}.
   *
   * @param aLeftColor {@link RGBA} - left color
   * @param aRightColor {@link RGBA} - right color
   * @param aLineInfo {@link TsLineInfo} - line drawing info
   * @param aIsLeft boolean - <code>true</code> to draw left edge
   * @param aIsRight boolean - <code>true</code> to draw right edge
   * @param aIsTop boolean - <code>true</code> to draw top edge
   * @param aIsBottom boolean - <code>true</code> to draw bottom edge
   * @return {@link TsBorderInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsBorderInfo ofSingle( RGBA aLeftColor, RGBA aRightColor, TsLineInfo aLineInfo, boolean aIsLeft,
      boolean aIsRight, boolean aIsTop, boolean aIsBottom ) {
    return ofKind1( ETsBorderKind.SINGLE, aLeftColor, aRightColor, aLineInfo, aIsLeft, aIsRight, aIsTop, aIsBottom );
  }

  /**
   * Creates border info of kind {@link ETsBorderKind#DOUBLE}.
   *
   * @param aLeftColor {@link RGBA} - left color
   * @param aRightColor {@link RGBA} - right color
   * @param aLineInfo {@link TsLineInfo} - line drawing info
   * @param aIsLeft boolean - <code>true</code> to draw left edge
   * @param aIsRight boolean - <code>true</code> to draw right edge
   * @param aIsTop boolean - <code>true</code> to draw top edge
   * @param aIsBottom boolean - <code>true</code> to draw bottom edge
   * @return {@link TsBorderInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsBorderInfo ofDouble( RGBA aLeftColor, RGBA aRightColor, TsLineInfo aLineInfo, boolean aIsLeft,
      boolean aIsRight, boolean aIsTop, boolean aIsBottom ) {
    return ofKind1( ETsBorderKind.DOUBLE, aLeftColor, aRightColor, aLineInfo, aIsLeft, aIsRight, aIsTop, aIsBottom );
  }

  private static TsBorderInfo ofKind1( ETsBorderKind aKind, RGBA aLeftColor, RGBA aRightColor, TsLineInfo aLineInfo,
      boolean aIsLeft, boolean aIsRight, boolean aIsTop, boolean aIsBottom ) {
    TsNullArgumentRtException.checkNulls( aLeftColor, aRightColor, aLineInfo );
    IOptionSetEdit ops = new OptionSet();
    ops.setValobj( OPDEF_BORDER_KIND, aKind );
    IAtomicValue avLeftColor = avValobj( aLeftColor );
    if( !avLeftColor.equals( OPDEF_LEFT_RGBA.defaultValue() ) ) {
      OPDEF_LEFT_RGBA.setValue( ops, avLeftColor );
    }
    IAtomicValue avRightColor = avValobj( aRightColor );
    if( !avRightColor.equals( OPDEF_RIGHT_RGBA.defaultValue() ) ) {
      OPDEF_RIGHT_RGBA.setValue( ops, avRightColor );
    }
    IAtomicValue avLineInfo = avValobj( aLineInfo );
    if( !avLineInfo.equals( OPDEF_LINE_INFO.defaultValue() ) ) {
      OPDEF_LINE_INFO.setValue( ops, avLineInfo );
    }
    if( OPDEF_PAINT_LEFT.defaultValue().asBool() != aIsLeft ) {
      OPDEF_PAINT_LEFT.setValue( ops, avBool( aIsLeft ) );
    }
    if( OPDEF_PAINT_RIGHT.defaultValue().asBool() != aIsRight ) {
      OPDEF_PAINT_RIGHT.setValue( ops, avBool( aIsRight ) );
    }
    if( OPDEF_PAINT_TOP.defaultValue().asBool() != aIsTop ) {
      OPDEF_PAINT_TOP.setValue( ops, avBool( aIsTop ) );
    }
    if( OPDEF_PAINT_BOTTOM.defaultValue().asBool() != aIsBottom ) {
      OPDEF_PAINT_BOTTOM.setValue( ops, avBool( aIsBottom ) );
    }
    return new TsBorderInfo( ops );
  }

  /**
   * Creates border as single color solid rectangle.
   *
   * @param aWidth int - the line width
   * @param aRgba {@link RGBA} - the color
   * @return {@link TsBorderInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsBorderInfo createSimpleBorder( int aWidth, RGBA aRgba ) {
    return ofSingle( aRgba, aRgba, TsLineInfo.ofWidth( aWidth ), true, true, true, true );
  }

  /**
   * Creates border as two-color solid rectangle.
   *
   * @param aWidth int - the line width
   * @param aLeftTopRgba {@link RGBA} - color of the left and top edges
   * @param aRightBottomRgba {@link RGBA} - color of the right and bottom edges
   * @return TsBorderInfo - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsBorderInfo createSingleBorder( int aWidth, RGBA aLeftTopRgba, RGBA aRightBottomRgba ) {
    return ofSingle( aLeftTopRgba, aRightBottomRgba, TsLineInfo.ofWidth( aWidth ), true, true, true, true );
  }

  /**
   * Creates border as two-color double solid line rectangle.
   *
   * @param aWidth int - the line width
   * @param aLeftTopRgba {@link RGBA} - color of the left and top edges
   * @param aRightBottomRgba {@link RGBA} - color of the right and bottom edges
   * @return TsBorderInfo - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsBorderInfo createDoubleBorder( int aWidth, RGBA aLeftTopRgba, RGBA aRightBottomRgba ) {
    return ofDouble( aLeftTopRgba, aRightBottomRgba, TsLineInfo.ofWidth( aWidth ), true, true, true, true );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the border kind.
   *
   * @return {@link ETsBorderKind} - the border kind
   */
  public ETsBorderKind kind() {
    return OPDEF_BORDER_KIND.getValue( options ).asValobj();
  }

  /**
   * Returns border drawing parameters as {@link IOptionSet} with options listed in {@link #ALL_DEFS}.
   *
   * @return {@link IOptionSet} - border drawing parameters
   */
  public IOptionSet options() {
    return options;
  }

  /**
   * Returns the color settings for the left and top border lines.
   *
   * @return RGBA - left and top line color
   */
  public RGBA leftTopRGBA() {
    return options.getValobj( OPDEF_LEFT_RGBA );
  }

  /**
   * Returns the color settings for the right and bottom border lines.
   *
   * @return RGBA - right and bottom line color
   */
  public RGBA rightBottomRGBA() {
    return options.getValobj( OPDEF_RIGHT_RGBA );
  }

  /**
   * Returns the line width.
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
   * Determines whether to draw the left side of the border.
   *
   * @return boolean - <code>true</code> to draw
   */
  public boolean shouldPaintLeft() {
    return options.getBool( OPDEF_PAINT_LEFT );
  }

  /**
   * Determines whether to draw the top side of the border.
   *
   * @return boolean - <code>true</code> to draw
   */
  public boolean shouldPaintTop() {
    return options.getBool( OPDEF_PAINT_TOP );
  }

  /**
   * Determines whether to draw the right side of the border.
   *
   * @return boolean - <code>true</code> to draw
   */
  public boolean shouldPaintRight() {
    return options.getBool( OPDEF_PAINT_RIGHT );
  }

  /**
   * Determines whether to draw the bottom side of the border.
   *
   * @return boolean - <code>true</code> to draw
   */
  public boolean shouldPaintBottom() {
    return options.getBool( OPDEF_PAINT_BOTTOM );
  }

  /**
   * Determines whether to draw the left side of the border.
   * <p>
   * Returns simply ANDed flags {@link #shouldPaintLeft()}, {@link #shouldPaintTop()}, {@link #shouldPaintRight()} and
   * {@link #shouldPaintBottom()}.
   *
   * @return boolean - <code>true</code> to draw
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
