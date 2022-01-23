package org.toxsoft.tslib.bricks.keeper.std;

import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.bricks.strio.*;
import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringLinkedBundleList;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link IStringList} keeper.
 * <p>
 * Returned value may be safely casted to the {@link IStringListEdit}.
 *
 * @author hazard157
 */
public class StringListKeeper
    extends AbstractEntityKeeper<IStringList> {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "StringList"; //$NON-NLS-1$

  /**
   * Keeper singleton (does not indents text representation).
   */
  public static final IEntityKeeper<IStringList> KEEPER = new StringListKeeper( false );

  /**
   * Keeper singleton (does indents text representation).
   */
  public static final IEntityKeeper<IStringList> KEEPER_INDENTED = new StringListKeeper( true );

  /**
   * Keeper compatibility mode singleton (does not indents text representation).
   *
   * @deprecated use only for importing old data
   */
  @Deprecated
  public static final IEntityKeeper<IStringList> COMPAT_KEEPER = new StringListKeeper( false, true );

  /**
   * Keeper compatibility mode singleton (does indents text representation).
   *
   * @deprecated use only for importing old data
   */
  @Deprecated
  public static final IEntityKeeper<IStringList> COMPAT_KEEPER_INDENTED = new StringListKeeper( true, true );

  private final boolean indent;
  private final boolean compatibilityMode;

  private StringListKeeper( boolean aIndent ) {
    this( aIndent, false );
  }

  private StringListKeeper( boolean aIndent, boolean aCompatibilityMode ) {
    super( IStringList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, IStringList.EMPTY );
    indent = aIndent;
    compatibilityMode = aCompatibilityMode;
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IStringList aEntity ) {
    if( compatibilityMode ) {
      aSw.writeChar( CHAR_SET_BEGIN );
    }
    else {
      aSw.writeChar( CHAR_ARRAY_BEGIN );
    }
    for( int i = 0, n = aEntity.size(); i < n; i++ ) {
      String s = aEntity.get( i );
      if( StridUtils.isValidIdPath( s ) ) {
        aSw.writeAsIs( s );
      }
      else {
        aSw.writeQuotedString( s );
      }
      if( i < n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
      }
      if( indent ) {
        aSw.writeEol();
      }
    }
    if( compatibilityMode ) {
      aSw.writeChar( CHAR_SET_END );
    }
    else {
      aSw.writeChar( CHAR_ARRAY_END );
    }
  }

  @Override
  protected IStringList doRead( IStrioReader aSr ) {
    IStringListEdit result = new StringLinkedBundleList();
    if( compatibilityMode ) {
      if( aSr.readSetBegin() ) {
        do {
          char ch = aSr.peekChar( EStrioSkipMode.SKIP_BYPASSED );
          if( ch == CHAR_QUOTE ) {
            result.add( aSr.readQuotedString() );
          }
          else {
            result.add( aSr.readIdPath() );
          }
        } while( aSr.readSetNext() );
      }
    }
    else {
      if( aSr.readArrayBegin() ) {
        do {
          char ch = aSr.peekChar( EStrioSkipMode.SKIP_BYPASSED );
          if( ch == CHAR_QUOTE ) {
            result.add( aSr.readQuotedString() );
          }
          else {
            result.add( aSr.readIdPath() );
          }
        } while( aSr.readArrayNext() );
      }
    }
    return result;
  }

}
