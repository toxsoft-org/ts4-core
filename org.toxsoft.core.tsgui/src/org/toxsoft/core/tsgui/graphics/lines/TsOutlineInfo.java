package org.toxsoft.core.tsgui.graphics.lines;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * How to draw an outline includes kind, line and color information.
 * <p>
 * This is an unmutable class.
 *
 * @author hazard157
 */
public final class TsOutlineInfo {

  // TODO outline editing VALED must be created and added to VALED detecting heuristics

  /**
   * Singletone of the outline info of the kind {@link ETsOutlineKind#NONE}.
   */
  public static final TsOutlineInfo NONE =
      new TsOutlineInfo( ETsOutlineKind.NONE, TsLineInfo.DEFAULT, new RGBA( 0, 0, 0, 0 ) );

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "TsOutlineInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<TsOutlineInfo> KEEPER =
      new AbstractEntityKeeper<>( TsOutlineInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TsOutlineInfo aEntity ) {
          ETsOutlineKind.KEEPER.write( aSw, aEntity.kind() );
          aSw.writeSeparatorChar();
          switch( aEntity.kind() ) {
            case NONE: {
              // NONE should not be written here, it is written as none object of AbstractEntityKeeper
              throw new TsInternalErrorRtException();
            }
            case SIMPLE: {
              TsLineInfo.KEEPER.write( aSw, aEntity.lineInfo() );
              aSw.writeSeparatorChar();
              RGBAKeeper.KEEPER.write( aSw, aEntity.color() );
              break;
            }
            // HERE may be added other outline kind cases
            default:
              throw new IllegalArgumentException( aEntity.kind().id() );
          }
        }

        @Override
        protected TsOutlineInfo doRead( IStrioReader aSr ) {
          ETsOutlineKind kind = ETsOutlineKind.KEEPER.read( aSr );
          switch( kind ) {
            case NONE: {
              // NONE should not be read here, it is read as none object of AbstractEntityKeeper
              throw new TsInternalErrorRtException();
            }
            case SIMPLE: {
              aSr.ensureSeparatorChar();
              TsLineInfo lineInfo = TsLineInfo.KEEPER.read( aSr );
              aSr.ensureSeparatorChar();
              RGBA coloe = RGBAKeeper.KEEPER.read( aSr );
              return ofSimple( lineInfo, coloe );
            }
            // HERE may be added other outline kind cases
            default:
              throw new IllegalArgumentException( kind.id() );
          }
        }
      };

  private final ETsOutlineKind kind;
  private final TsLineInfo     lineInfo;
  private final RGBA           color;

  /**
   * Private constructor, use static constructors or {@link #NONE} for instance creation.
   *
   * @param aKind {@link ETsOutlineKind} - outline kind
   * @param aLineInfo {@link TsLineInfo} - line drawing info
   * @param aColor {@link RGBA} - the color
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  private TsOutlineInfo( ETsOutlineKind aKind, TsLineInfo aLineInfo, RGBA aColor ) {
    TsNullArgumentRtException.checkNulls( aKind, aLineInfo, aColor );
    kind = aKind;
    lineInfo = aLineInfo;
    color = aColor;
  }

  /**
   * Simply returns {@link #NONE}.
   *
   * @return {@link TsOutlineInfo} - the instance of no outline info
   */
  public static TsOutlineInfo ofNone() {
    return NONE;
  }

  /**
   * Creates outline info of the kind {@link ETsOutlineKind#SIMPLE}.
   *
   * @param aLineInfo {@link TsLineInfo} - line drawing info
   * @param aColor {@link RGBA} - the color
   * @return {@link TsOutlineInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static TsOutlineInfo ofSimple( TsLineInfo aLineInfo, RGBA aColor ) {
    return new TsOutlineInfo( ETsOutlineKind.SIMPLE, aLineInfo, aColor );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the outline drawing kind.
   *
   * @return {@link ETsOutlineKind} - the outline drawing kind
   */
  public ETsOutlineKind kind() {
    return kind;
  }

  /**
   * Returns the outline line drawing info.
   *
   * @return {@link TsLineInfo} - the outline line drawing info
   */
  public TsLineInfo lineInfo() {
    return lineInfo;
  }

  /**
   * Returns the outline color.
   *
   * @return {@link RGBA} - the outline color
   */
  public RGBA color() {
    return color;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    switch( kind ) {
      case NONE:
        return kind.id();
      case SIMPLE:
        return String.format( "%s: line %s, color %s", kind.id(), lineInfo.toString(), color.toString() ); //$NON-NLS-1$
      default:
        throw new IllegalArgumentException( kind.id() );
    }
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj instanceof TsOutlineInfo that ) {
      if( this.kind == that.kind ) {
        switch( kind ) {
          case NONE:
            return true;
          case SIMPLE:
            return this.lineInfo.equals( that.lineInfo ) && this.color.equals( that.color );
          default:
            throw new TsNotAllEnumsUsedRtException( kind.id() );
        }
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + kind.hashCode();
    switch( kind ) {
      case NONE: {
        break;
      }
      case SIMPLE: {
        result = TsLibUtils.PRIME * result + lineInfo.hashCode();
        result = TsLibUtils.PRIME * result + color.hashCode();
        break;
      }
      default:
        throw new IllegalArgumentException( kind.id() );
    }
    return result;
  }

}
