package org.toxsoft.core.tslib.bricks.keeper;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
 * <li>{@link IEntityKeeper} implementation is designed to store/restore immutable entities (value-objects). However it
 * also may be user for mutable objects. Just keep in mind that restoring using {@link IEntityKeeper} always creates new
 * instances.</li>
 * </ul>
 * This keeper also supports storing "null-objects". "Null" objects must be singletons. Equals operator <code>==</code>
 * is used for "null-object" detection rather than {@link Object#equals(Object)} method.
 *
 * @author hazard157
 * @param <E> - type of kept elements
 */
public interface IEntityKeeper<E> {

  /**
   * The text representation of empty collection of any kind of the entities.
   */
  String STR_EMPTY_COLLECTION_REPRESENTATION = "[]"; //$NON-NLS-1$

  /**
   * {@link #STR_EMPTY_COLLECTION_REPRESENTATION} as a {@link EAtomicType#STRING} atomic value.
   */
  IAtomicValue STR_EMPTY_COLLECTION_REPRESENTATION_AV = avStr( STR_EMPTY_COLLECTION_REPRESENTATION );

  /**
   * Returns the class of the kept entity.
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
   * @throws TsIoRtException {@link IOException} occurred
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
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  E read( IStrioReader aSr );

  /**
   * Stores entity to string.
   *
   * @param aEntity &lt;E&gt; - entity to be stored
   * @return String - text representation string
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  String ent2str( E aEntity );

  /**
   * Restores entity from string.
   *
   * @param aStr String - text representation string
   * @return &lt;E&gt; - restored entity
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
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
   * @throws TsIoRtException {@link IOException} occurred
   */
  void write( File aFile, E aEntity );

  /**
   * Restores entity from the file.
   *
   * @param aFile {@link File} - the source file
   * @return &lt;E&gt; - restored entity
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  E read( File aFile );

  /**
   * Stores text representation of the collection to the {@link String}.
   *
   * @param aColl {@link ITsCollection} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @return String - text representation string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  String coll2str( ITsCollection<E> aColl, boolean aIndented );

  /**
   * Reads text representation of the collection from the string.
   *
   * @param aCollAsString String - text representation string
   * @return {@link IListEdit} - restored collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IListEdit<E> str2coll( String aCollAsString );

  /**
   * Stores text representation of the map (where keys are an IDpaths) to the {@link String}.
   *
   * @param aMap {@link IStringMap} - map to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @return String - text representation string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException {@link IOException} occurred
   */
  String idmap2str( IStringMap<E> aMap, boolean aIndented );

  /**
   * Reads text representation of the (where keys are an IDpaths) from the string.
   *
   * @param aMapAsString String - text representation string
   * @return {@link IStringMapEdit} - restored map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IStringMapEdit<E> str2idmap( String aMapAsString );

  /**
   * Stores text representation of the map (where keys are an IDpaths) to the {@link String}.
   *
   * @param aMap {@link IStringMap} - map to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @return String - text representation string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException {@link IOException} occurred
   */
  String strmap2str( IStringMap<E> aMap, boolean aIndented );

  /**
   * Reads text representation of the (where keys are an IDpaths) from the string.
   *
   * @param aMapAsString String - text representation string
   * @return {@link IStringMapEdit} - restored map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IStringMapEdit<E> str2strmap( String aMapAsString );

  /**
   * Stores text representation of the collection.
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aColl {@link ITsCollection} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  void writeColl( IStrioWriter aSw, ITsCollection<E> aColl, boolean aIndented );

  /**
   * Stores text representation of the collection to the file.
   *
   * @param aFile {@link File} - file to store text representation
   * @param aColl {@link ITsCollection} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  void writeColl( File aFile, ITsCollection<E> aColl, boolean aIndented );

  /**
   * Stores text representation of the map (where keys are an IDpaths).
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aMap {@link IStringMap} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException {@link IOException} occurred
   */
  void writeStridMap( IStrioWriter aSw, IStringMap<E> aMap, boolean aIndented );

  /**
   * Stores text representation of the map (where keys are an IDpaths) to the file.
   *
   * @param aFile {@link File} - file to store text representation
   * @param aMap {@link IStringMap} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException {@link IOException} occurred
   */
  void writeStridMap( File aFile, IStringMap<E> aMap, boolean aIndented );

  /**
   * Stores text representation of the map (where keys are any String).
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aMap {@link IStringMap} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  void writeStringMap( IStrioWriter aSw, IStringMap<E> aMap, boolean aIndented );

  /**
   * Stores text representation of the map (where keys are any String) to the file.
   *
   * @param aFile {@link File} - file to store text representation
   * @param aMap {@link IStringMap} - collection to be stored
   * @param aIndented boolean - hint to write collection in indented (human-readable) form
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  void writeStringMap( File aFile, IStringMap<E> aMap, boolean aIndented );

  /**
   * Reads text representation of the collection from the stream.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return {@link IListEdit} - restored collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IListEdit<E> readColl( IStrioReader aSr );

  /**
   * Reads text representation of the collection from the file.
   *
   * @param aFile {@link File} - file to be read
   * @return {@link IListEdit} - restored collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IListEdit<E> readColl( File aFile );

  /**
   * Reads text representation of the map (where keys are an IDpaths) from the stream.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return {@link IStringMapEdit} - restored map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IStringMapEdit<E> readStridMap( IStrioReader aSr );

  /**
   * Reads text representation of the map (where keys are an IDpaths) from the file.
   *
   * @param aFile {@link File} - file to be read
   * @return {@link IStringMapEdit} - restored map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IStringMapEdit<E> readStridMap( File aFile );

  /**
   * Reads text representation of the map (where keys are any String) from the stream.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return {@link IStringMapEdit} - restored map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IStringMapEdit<E> readStringMap( IStrioReader aSr );

  /**
   * Reads text representation of the map (where keys are any String) from the file.
   *
   * @param aFile {@link File} - file to be read
   * @return {@link IStringMapEdit} - restored map
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  IStringMapEdit<E> readStringMap( File aFile );

  /**
   * Reads text representation of the collection from the stream.
   * <p>
   * Restored elements will be added to the destination collection.
   *
   * @param <T> - expected type of the collection elements
   * @param aSr {@link IStrioReader} - input stream
   * @param aColl {@link ITsCollectionEdit} - destination collection
   * @return {@link ITsCollectionEdit} - always returns the argument collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  <T extends ITsCollectionEdit<E>> T readColl( IStrioReader aSr, ITsCollectionEdit<E> aColl );

  /**
   * Reads text representation of the collection from the file.
   * <p>
   * Restored elements will be added to the destination collection.
   *
   * @param <T> - expected type of the collection elements
   * @param aFile {@link File} - file to be read
   * @param aColl {@link ITsCollectionEdit} - destination collection
   * @return {@link ITsCollectionEdit} - always returns the argument collection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   * @throws StrioRtException invalid text representation format
   */
  <T extends ITsCollectionEdit<E>> T readColl( File aFile, ITsCollectionEdit<E> aColl );

  // ------------------------------------------------------------------------------------
  // In-line methods for convenience
  //

  /**
   * Stores unindented text representation of the collection to the {@link String}.
   *
   * @param aColl {@link ITsCollection} - collection to be stored
   * @return String - text representation string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
   */
  default String coll2str( ITsCollection<E> aColl ) {
    return coll2str( aColl, false );
  }

  /**
   * Stores unindented text representation of the map (where keys are an IDpaths) to the {@link String}.
   *
   * @param aMap {@link IStringMap} - map to be stored
   * @return String - text representation string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any key in the map is not an IDpath
   * @throws TsIoRtException {@link IOException} occurred
   */
  default String idmap2str( IStringMap<E> aMap ) {
    return idmap2str( aMap, false );
  }

  /**
   * Writes (stores) entity to the text representation stream.
   * <p>
   * If {@link #isEnclosed()} = <code>true</code> this method is the same as {@link #write(IStrioWriter, Object)}.
   *
   * @param aSw {@link IStrioWriter} - text representation writer stream
   * @param aEntity &lt;E&gt; - entity to be stored
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occurred
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
   * @throws TsIoRtException {@link IOException} occurred
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
