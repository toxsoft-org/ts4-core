package org.toxsoft.core.tslib.bricks.keeper;

import static org.toxsoft.core.tslib.bricks.strio.EStrioSkipMode.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IEntityKeeper} implementaion base.
 *
 * @author hazard157
 * @param <E> - type of keeped elements
 */
public abstract class AbstractEntityKeeper<E>
    implements IEntityKeeper<E> {

  /**
   * Determines who and how encloses text representation in parentheses.
   *
   * @author hazard157
   */
  public enum EEncloseMode {

    /**
     * Text representation is not enclosed in any parentheses.
     */
    NOT_IN_PARENTHESES,

    /**
     * Text representation is enclosed by base class {@link AbstractEntityKeeper}.
     * <p>
     * There is no need for {@link AbstractEntityKeeper#doWrite(IStrioWriter, Object)} to write openinig and closing
     * parenthesis.
     */
    ENCLOSES_BASE_CLASS,

    /**
     * Text representation is enclosed by the keeper implementation in
     * {@link AbstractEntityKeeper#doWrite(IStrioWriter, Object)}.
     */
    ENCLOSES_KEEPER_IMPLEMENTATION

  }

  private final Class<E>     entityClass;
  private final EEncloseMode encloseMode;
  private final E            noneObject;

  /**
   * Constructor.
   *
   * @param aEntityClass {@link Class} - an entity class
   * @param aEncloseMode {@link EEncloseMode} - who and how encloses text representation in parentheses
   * @param aNoneObject &lt;E&gt; - none object used to read empty parentheses or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public AbstractEntityKeeper( Class<E> aEntityClass, EEncloseMode aEncloseMode, E aNoneObject ) {
    TsNullArgumentRtException.checkNulls( aEntityClass, aEncloseMode );
    entityClass = aEntityClass;
    encloseMode = aEncloseMode;
    noneObject = aNoneObject;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  protected void internalWriteEntity( IStrioWriter aSw, E aEntity ) {
    if( encloseMode != EEncloseMode.ENCLOSES_BASE_CLASS ) {
      doWrite( aSw, aEntity );
      return;
    }
    aSw.writeChar( CHAR_SET_BEGIN );
    // none object will be written as empty parentheses
    if( noneObject != null && aEntity == noneObject ) {
      aSw.writeChar( CHAR_SET_END );
      return;
    }
    doWrite( aSw, aEntity );
    aSw.writeChar( CHAR_SET_END );
  }

  protected E internalReadEntity( IStrioReader aSr ) {
    if( encloseMode != EEncloseMode.ENCLOSES_BASE_CLASS ) {
      return doRead( aSr );
    }
    aSr.ensureChar( CHAR_SET_BEGIN );
    // empty parentheses will be read as none object
    if( noneObject != null && aSr.peekChar( SKIP_COMMENTS ) == CHAR_SET_END ) {
      aSr.nextChar();
      return noneObject;
    }
    E e = doRead( aSr );
    aSr.ensureChar( CHAR_SET_END );
    return e;
  }

  private void internalWriteColl( IStrioWriter aSw, ITsCollection<E> aColl, boolean aIndented ) {
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    if( !aColl.isEmpty() ) {
      if( aIndented ) {
        aSw.incNewLine();
      }
      int i = 0, n = aColl.size();
      for( E item : aColl ) {
        internalWriteEntity( aSw, item );
        if( i < n - 1 ) {
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          if( aIndented ) {
            aSw.writeEol();
          }
        }
        ++i;
      }
      if( aIndented ) {
        aSw.decNewLine();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
  }

  private IListEdit<E> internalReadColl( IStrioReader aSr ) {
    IListEdit<E> coll = new ElemLinkedBundleList<>();
    if( aSr.readArrayBegin() ) {
      do {
        E item = internalReadEntity( aSr );
        coll.add( item );
      } while( aSr.readArrayNext() );
    }
    return coll;
  }

  private void internalReadColl( IStrioReader aSr, ITsCollectionEdit<E> aColl ) {
    if( aSr.readArrayBegin() ) {
      do {
        E item = internalReadEntity( aSr );
        aColl.add( item );
      } while( aSr.readArrayNext() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IEntityKeeper
  //

  @Override
  public Class<E> entityClass() {
    return entityClass;
  }

  @Override
  public E noneObject() {
    return noneObject;
  }

  @Override
  public boolean isEnclosed() {
    switch( encloseMode ) {
      case NOT_IN_PARENTHESES:
        return false;
      case ENCLOSES_BASE_CLASS:
      case ENCLOSES_KEEPER_IMPLEMENTATION:
        return true;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public void write( IStrioWriter aSw, E aEntity ) {
    TsNullArgumentRtException.checkNulls( aSw, aEntity );
    internalWriteEntity( aSw, aEntity );
  }

  @Override
  public E read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    return internalReadEntity( aSr );
  }

  @Override
  public String ent2str( E aEntity ) {
    if( aEntity == null ) {
      throw new TsNullArgumentRtException();
    }
    StringBuilder sb = new StringBuilder();
    ICharOutputStream chOut = new CharOutputStreamAppendable( sb );
    IStrioWriter sw = new StrioWriter( chOut );
    internalWriteEntity( sw, aEntity );
    return sb.toString();
  }

  @Override
  public E str2ent( String aStr ) {
    ICharInputStream chIn = new CharInputStreamString( aStr );
    IStrioReader sr = new StrioReader( chIn );
    return internalReadEntity( sr );
  }

  @Override
  public void write( File aFile, E aEntity ) {
    TsNullArgumentRtException.checkNulls( aFile, aEntity );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      internalWriteEntity( sw, aEntity );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
  }

  @Override
  public E read( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      return internalReadEntity( sr );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
  }

  @Override
  public String coll2str( ITsCollection<E> aColl ) {
    TsNullArgumentRtException.checkNull( aColl );
    StringBuilder sb = new StringBuilder();
    ICharOutputStream chOut = new CharOutputStreamAppendable( sb );
    IStrioWriter sw = new StrioWriter( chOut );
    internalWriteColl( sw, aColl, false );
    return sb.toString();
  }

  @Override
  public IListEdit<E> str2coll( String aCollAsString ) {
    ICharInputStream chIn = new CharInputStreamString( aCollAsString );
    IStrioReader sr = new StrioReader( chIn );
    return readColl( sr );
  }

  @Override
  public void writeColl( IStrioWriter aSw, ITsCollection<E> aColl, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aSw, aColl );
    internalWriteColl( aSw, aColl, aIndented );
  }

  @Override
  public void writeColl( File aFile, ITsCollection<E> aColl, boolean aIndented ) {
    TsNullArgumentRtException.checkNulls( aFile, aColl );
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      internalWriteColl( sw, aColl, aIndented );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
  }

  @Override
  public IListEdit<E> readColl( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    return internalReadColl( aSr );
  }

  @Override
  public IListEdit<E> readColl( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      return internalReadColl( sr );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <T extends ITsCollectionEdit<E>> T readColl( IStrioReader aSr, ITsCollectionEdit<E> aColl ) {
    TsNullArgumentRtException.checkNulls( aSr, aColl );
    internalReadColl( aSr, aColl );
    return (T)aColl;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <T extends ITsCollectionEdit<E>> T readColl( File aFile, ITsCollectionEdit<E> aColl ) {
    TsNullArgumentRtException.checkNulls( aFile, aColl );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      internalReadColl( sr, aColl );
    }
    catch( Exception ex ) {
      throw new TsIoRtException( ex );
    }
    return (T)aColl;
  }

  // ------------------------------------------------------------------------------------
  // Abstract methods
  //

  /**
   * The same as {@link #write(IStrioWriter, Object)} but guaranteed non-<code>null</code> arguments.
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream, not <code>null</code>
   * @param aEntity &lt;E&gt; - entity to be stored, not <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  protected abstract void doWrite( IStrioWriter aSw, E aEntity );

  /**
   * The same as {@link #read(IStrioReader)} but guaranteed non-<code>null</code> argument.
   *
   * @param aSr {@link IStrioReader} - text representation reader stream, not <code>null</code>
   * @return &lt;E&gt; - loaded entity, must not be <code>null</code>
   * @throws TsIoRtException underlying (if any) stream reading error
   * @throws StrioRtException syntaxic error
   */
  protected abstract E doRead( IStrioReader aSr );

}
