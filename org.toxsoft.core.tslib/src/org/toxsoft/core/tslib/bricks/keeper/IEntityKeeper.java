package org.toxsoft.core.tslib.bricks.keeper;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.File;
import java.io.IOException;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.basis.ITsCollectionEdit;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * The interface for storing and loading entities into a text representation.
 * <p>
 * Text representation format is determined by implementation so that {@link #read(IStrioReader)} must fully and
 * correctly restore entity from written by {@link #write(IStrioWriter, Object)} text.
 * <p>
 * There are two interfaces {@link IEntityKeeper} and {@link IKeepableEntity} doing the same work but with different
 * kind of entities.
 * <ul>
 * <li>mixin interface {@link IKeepableEntity} is designed to store/restore mutable objects contents so that reference
 * to object remains unchanged;</li>
 * <li>{@link IEntityKeeper} implementation is desgned to store/restore immutable entities (value-objects). However it
 * also may be user for mutable objects. Just keep in mind that restoring using {@link IEntityKeeper} always creates new
 * instances.</li>
 * </ul>
 * This keeper also supports storing "null-objects" "Null" objects must be singletons. Equals operator <code>==</code>
 * is used for "null-object" detection rather than {@link Object#equals(Object)} method.
 *
 * @author hazard157
 * @param <E> - type of keeped elements
 */
public interface IEntityKeeper<E> {

  /**
   * The text representaton of empty collection of any kind of the entities.
   */
  String STR_EMPTY_COLLECTION_REPRESENTATION = "[]"; //$NON-NLS-1$

  /**
   * Returns the class of the keeped entity.
   *
   * @return {@link Class}&lt;E&gt; - an entity class
   */
  Class<E> entityClass();

  /**
   * Returns "none" object as set in constructor.
   *
   * @return &lt;E&gt; - "none" object, may be <code>null</code>
   */
  E noneObject();

  /**
   * Determines if content is surrounded by the braces {} or brackets [].
   *
   * @return boolean - true= first and last characters are [] or {}
   */
  boolean isEnclosed();

  /**
   * Writes (stores) entity to the text representation stream.
   * <p>
   * This is the basic writer (storage) method. All other writer methods are using this one.
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aEntity &lt;E&gt; - entity to be stored
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  void write( IStrioWriter aSw, E aEntity );

  /**
   * Reads (restores) entity from the text representation stream.
   * <p>
   * This is the basic reader (restore) method. All other reader methods are using this one.
   *
   * @param aSr {@link IStrioReader} - text representation reader stream
   * @return &lt;E&gt; - restored entity
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  E read( IStrioReader aSr );

  /**
   * Stores entity to string.
   *
   * @param aEntity &lt;E&gt; - entity to be stored
   * @return String - text representation string
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  String ent2str( E aEntity );

  /**
   * Restores entity from string.
   *
   * @param aStr String - text representation string
   * @return &lt;E&gt; - restored entity
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  E str2ent( String aStr );

  /**
   * Stores entity to the file.
   * <p>
   * The existing file will be overwritten, non-existing file will be created.
   *
   * @param aFile {@link File} - the destination file
   * @param aEntity &lt;E&gt; - entity to be stored
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  void write( File aFile, E aEntity );

  /**
   * Restores entity from the file.
   *
   * @param aFile {@link File} - the source file
   * @return &lt;E&gt; - restored entity
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  E read( File aFile );

  /**
   * Stores text representaion of the collection to the {@link String}.
   *
   * @param aColl {@link ITsCollection} - collection to be stored
   * @return String - text representation string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  String coll2str( ITsCollection<E> aColl );

  /**
   * Reads text representaion of the collection from the string.
   *
   * @param aCollAsString String - text representation string
   * @return {@link IListEdit} - restored colleaction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  IListEdit<E> str2coll( String aCollAsString );

  /**
   * Stores text representaion of the collection.
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aColl {@link ITsCollection} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  void writeColl( IStrioWriter aSw, ITsCollection<E> aColl, boolean aIndented );

  /**
   * Stores text representaion of the collection to the file.
   *
   * @param aFile {@link File} - file to store text representation
   * @param aColl {@link ITsCollection} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  void writeColl( File aFile, ITsCollection<E> aColl, boolean aIndented );

  /**
   * Reads text representaion of the collection from the stream.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return {@link IListEdit} - restored colleaction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  IListEdit<E> readColl( IStrioReader aSr );

  /**
   * Reads text representaion of the collection from the file.
   *
   * @param aFile {@link File} - file to be read
   * @return {@link IListEdit} - restored colleaction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  IListEdit<E> readColl( File aFile );

  /**
   * Reads text representaion of the collection from the stream.
   * <p>
   * Restored elements will be added to the destination collection.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @param aColl {@link ITsCollectionEdit} - destination colleaction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  void readColl( IStrioReader aSr, ITsCollectionEdit<E> aColl );

  /**
   * Reads text representaion of the collection from the file.
   * <p>
   * Restored elements will be added to the destination collection.
   *
   * @param aFile {@link File} - file to be read
   * @param aColl {@link ITsCollectionEdit} - destination colleaction
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  void readColl( File aFile, ITsCollectionEdit<E> aColl );

  // ------------------------------------------------------------------------------------
  // Inline methods for convinience
  //

  /**
   * Writes (stores) entity to the text representation stream.
   * <p>
   * If {@link #isEnclosed()} = <code>true</code> this method is the same as {@link #write(IStrioWriter, Object)}.
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aEntity &lt;E&gt; - entity to be stored
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   */
  default void writeEnclosed( IStrioWriter aSw, E aEntity ) {
    if( isEnclosed() ) {
      write( aSw, aEntity );
      return;
    }
    aSw.writeChar( CHAR_SET_BEGIN );
    write( aSw, aEntity );
    aSw.writeChar( CHAR_SET_END );
  }

  /**
   * Reads (restores) entity from the enclosed text representation stream.
   * <p>
   * If {@link #isEnclosed()} = <code>true</code> this method is the same as {@link #read(IStrioReader)}.
   *
   * @param aSr {@link IStrioReader} - text representation reader stream
   * @return &lt;E&gt; - restored entity
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured
   * @throws StrioRtException invalid text representation format
   */
  default E readEnclosed( IStrioReader aSr ) {
    if( isEnclosed() ) {
      return read( aSr );
    }
    aSr.ensureChar( CHAR_SET_BEGIN );
    E e = read( aSr );
    aSr.ensureChar( CHAR_SET_END );
    return e;
  }

}
