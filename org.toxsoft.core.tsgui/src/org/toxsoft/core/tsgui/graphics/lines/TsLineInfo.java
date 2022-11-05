package org.toxsoft.core.tsgui.graphics.lines;

import static org.toxsoft.core.tsgui.graphics.lines.ETsLineCapStyle.*;
import static org.toxsoft.core.tsgui.graphics.lines.ETsLineJoinStyle.*;
import static org.toxsoft.core.tsgui.graphics.lines.ETsLineType.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * All attributes to draw line, the tsgui equivalent of {@link LineAttributes}.
 * <p>
 * There is convinience methods {@link #lineInfoFromGc(GC)}, {@link #setToGc(GC)},
 * {@link #fromLineAttributes(LineAttributes)}, {@link #toLineAttributes(LineAttributes)}.
 *
 * @author hazard157
 */
public final class TsLineInfo {

  /**
   * Default solid line of1 pixel width.
   */
  public static final TsLineInfo DEFAULT = new TsLineInfo( 1, SOLID, FLAT, MITER, TsLibUtils.EMPTY_ARRAY_OF_INTS );

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsLineInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsLineInfo> KEEPER =
      new AbstractEntityKeeper<>( TsLineInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, DEFAULT ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsLineInfo aEntity ) {
          aSw.writeInt( aEntity.width );
          aSw.writeSeparatorChar();
          ETsLineType.KEEPER.write( aSw, aEntity.type );
          aSw.writeSeparatorChar();
          ETsLineCapStyle.KEEPER.write( aSw, aEntity.capStyle );
          aSw.writeSeparatorChar();
          ETsLineJoinStyle.KEEPER.write( aSw, aEntity.joinStyle );
          aSw.writeSeparatorChar();
          StrioUtils.writeIntArray( aSw, aEntity.dash );
        }

        @Override
        protected TsLineInfo doRead( IStrioReader aSr ) {
          int width = aSr.readInt();
          aSr.ensureSeparatorChar();
          ETsLineType lineType = ETsLineType.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ETsLineCapStyle lineCapStyle = ETsLineCapStyle.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ETsLineJoinStyle lineJoinStyle = ETsLineJoinStyle.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          int[] dash = StrioUtils.readIntArray( aSr );
          return new TsLineInfo( width, lineType, lineCapStyle, lineJoinStyle, dash );
        }

      };

  private final int width;

  private final ETsLineType      type;
  private final ETsLineCapStyle  capStyle;
  private final ETsLineJoinStyle joinStyle;
  private final int[]            dash;

  /**
   * Constructor.
   * <p>
   * For any line type except {@link ETsLineType#CUSTOM} value of the argument <code>aDash</code> is ignored and may be
   * <code>null</code>. If for custom type value of the argument <code>aDash</code> is null or an empty list then line
   * style will be reset to {@link ETsLineType#SOLID}.
   * <p>
   * Line width will be adjusted to be 1 or greater.
   *
   * @param aWidth int - line thikness
   * @param aType {@link ETsLineType} - line type
   * @param aCapStyle {@link ETsLineCapStyle} - cap style
   * @param aJoinStyle {@link ETsLineJoinStyle} - join style
   * @param aDash {@link IIntList} - custom line dash as for {@link GC#setLineDash(int[])} or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsLineInfo( int aWidth, ETsLineType aType, ETsLineCapStyle aCapStyle, ETsLineJoinStyle aJoinStyle,
      IIntList aDash ) {
    TsNullArgumentRtException.checkNulls( aType, aCapStyle, aJoinStyle );
    if( aDash == null || aDash.isEmpty() ) {
      type = ETsLineType.SOLID;
      dash = TsLibUtils.EMPTY_ARRAY_OF_INTS;
    }
    else {
      type = aType;
      dash = aDash.toValuesArray();
    }
    width = aWidth >= 1 ? aWidth : 1;
    capStyle = aCapStyle;
    joinStyle = aJoinStyle;
  }

  private TsLineInfo( int aWidth, ETsLineType aType, ETsLineCapStyle aCapStyle, ETsLineJoinStyle aJoinStyle,
      int[] aDash ) {
    width = aWidth >= 1 ? aWidth : 1;
    capStyle = aCapStyle;
    joinStyle = aJoinStyle;
    type = aType;
    dash = aDash;
  }

  /**
   * Creates solide line info of the given width with SWT default other attributes.
   *
   * @param aWidth int - the line width
   * @return {@link TsLineInfo} - created instance
   */
  public static TsLineInfo ofWidth( int aWidth ) {
    return new TsLineInfo( aWidth, ETsLineType.SOLID, ETsLineCapStyle.FLAT, ETsLineJoinStyle.MITER, IIntList.EMPTY );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the line type.
   *
   * @return {@link ETsLineType} - the line type
   */
  public ETsLineType type() {
    return type;
  }

  /**
   * Returns the line cap style.
   *
   * @return {@link ETsLineCapStyle} - the line cap style
   */
  public ETsLineCapStyle capStyle() {
    return capStyle;
  }

  /**
   * Returns the line join style.
   *
   * @return {@link ETsLineJoinStyle} - the line join style
   */
  public ETsLineJoinStyle joinStyle() {
    return joinStyle;
  }

  /**
   * returns the line width.
   *
   * @return int - the line width in pixels
   */
  public int width() {
    return width;
  }

  /**
   * Returns the custom line dash.
   *
   * @return int[] - line dash, never is <code>null</code>
   */
  public int[] dash() {
    return dash;
  }

  // ------------------------------------------------------------------------------------
  // API fro convinience
  //

  /**
   * Creates {@link TsLineInfo} from SWT {@link LineAttributes}.
   *
   * @param aLineAttrs {@link LineAttributes} - SWT line attributes
   * @return {@link TsLineInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument has invalid field values
   */
  public static TsLineInfo fromLineAttributes( LineAttributes aLineAttrs ) {
    int width = (int)aLineAttrs.width;
    ETsLineType lineType = ETsLineType.getBySwtStyle( aLineAttrs.style );
    ETsLineCapStyle lineCapStyle = ETsLineCapStyle.getBySwtStyle( aLineAttrs.cap );
    ETsLineJoinStyle lineJoinStyle = ETsLineJoinStyle.getBySwtStyle( aLineAttrs.join );
    IIntList dash = IIntList.EMPTY;
    if( lineType == ETsLineType.CUSTOM ) {
      float[] dd = aLineAttrs.dash;
      if( dd != null && dd.length > 0 ) {
        IIntListEdit ll = new IntArrayList();
        for( int i = 0; i < dd.length; i++ ) {
          ll.add( (int)dd[i] );
        }
        dash = ll;
      }
    }
    return new TsLineInfo( width, lineType, lineCapStyle, lineJoinStyle, dash );
  }

  /**
   * Updates argument from this instance.
   *
   * @param aLineAttrs {@link LineAttributes} - SWT line attributes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void toLineAttributes( LineAttributes aLineAttrs ) {
    TsNullArgumentRtException.checkNull( aLineAttrs );
    aLineAttrs.width = width;
    aLineAttrs.style = type.getSwtStyle();
    aLineAttrs.cap = capStyle.getSwtStyle();
    aLineAttrs.join = joinStyle.getSwtStyle();
    if( type == ETsLineType.CUSTOM ) {
      aLineAttrs.dash = new float[dash.length];
      for( int i = 0; i < dash.length; i++ ) {
        aLineAttrs.dash[i] = dash[i];
      }
    }
  }

  /**
   * Creates {@link TsLineInfo} from current setting of {@link GC}.
   *
   * @param aGc {@link GC} - drawing context
   * @return {@link TsLineInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsLineInfo lineInfoFromGc( GC aGc ) {
    TsNullArgumentRtException.checkNull( aGc );
    int width = aGc.getLineWidth();
    ETsLineType lineType = ETsLineType.getBySwtStyle( aGc.getLineStyle() );
    ETsLineCapStyle lineCapStyle = ETsLineCapStyle.getBySwtStyle( aGc.getLineCap() );
    ETsLineJoinStyle lineJoinStyle = ETsLineJoinStyle.getBySwtStyle( aGc.getLineJoin() );
    IIntList dash = IIntList.EMPTY;
    if( lineType == ETsLineType.CUSTOM ) {
      int[] dd = aGc.getLineDash();
      if( dd != null && dd.length > 0 ) {
        dash = new IntArrayList( dd );
      }
    }
    return new TsLineInfo( width, lineType, lineCapStyle, lineJoinStyle, dash );
  }

  /**
   * Applies line attributes from this instance to the {@link GC}.
   *
   * @param aGc {@link GC} - drawing context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setToGc( GC aGc ) {
    TsNullArgumentRtException.checkNull( aGc );
    aGc.setLineWidth( width );
    aGc.setLineStyle( type.getSwtStyle() );
    aGc.setLineCap( capStyle.getSwtStyle() );
    aGc.setLineJoin( joinStyle.getSwtStyle() );
    if( type == ETsLineType.CUSTOM ) {
      aGc.setLineDash( dash );
    }
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "width=%d, type=%s, cap=%s, join=%s", //$NON-NLS-1$
        width, type.id(), capStyle.id(), joinStyle.id() );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj instanceof TsLineInfo that ) {
      if( width == that.width && type == that.type && capStyle == that.capStyle && joinStyle == that.joinStyle ) {
        if( dash.length != that.dash.length ) {
          return false;
        }
        for( int i = 0; i < dash.length; i++ ) {
          if( this.dash[i] != that.dash[i] ) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + width;
    result = TsLibUtils.PRIME * result + type.hashCode();
    result = TsLibUtils.PRIME * result + capStyle.hashCode();
    result = TsLibUtils.PRIME * result + joinStyle.hashCode();
    for( int i = 0; i < dash.length; i++ ) {
      result = TsLibUtils.PRIME * dash[i];
    }
    return result;
  }

}
