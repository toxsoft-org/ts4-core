package org.toxsoft.core.tslib.utils.files;

import static org.toxsoft.core.tslib.utils.files.EFsObjKind.*;

import java.io.File;

import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.SingleStringList;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base for simple file filtering via {@link File#listFiles(java.io.FileFilter)}.
 *
 * @author hazard157
 */
public class TsFileFilter
    implements java.io.FileFilter {

  /**
   * Accept only unhidden directories.
   */
  public static final TsFileFilter FF_DIRS = new TsFileFilter( DIR, IStringList.EMPTY, false, false );

  /**
   * Accept any directories including hidden ones.
   */
  public static final TsFileFilter FF_DIRS_HIDDEN = new TsFileFilter( DIR, IStringList.EMPTY, true, false );

  /**
   * Accept only unhidden files.
   */
  public static final TsFileFilter FF_FILES = new TsFileFilter( FILE, IStringList.EMPTY, false, false );

  /**
   * Accept any files including hidden ones.
   */
  public static final TsFileFilter FF_FILES_HIDDEN = new TsFileFilter( FILE, IStringList.EMPTY, true, false );

  /**
   * Accept unhidden files and directories.
   */
  public static final TsFileFilter FF_ALL = new TsFileFilter( BOTH, IStringList.EMPTY, false, false );

  /**
   * Accept all files and directories including hidden ones.
   */
  public static final TsFileFilter FF_ALL_HIDDEN = new TsFileFilter( BOTH, IStringList.EMPTY, true, false );

  private final EFsObjKind  whatAccepted;
  private final boolean     acceptHidden;
  private final boolean     caseSensitive;
  private final IStringList extensions;
  private final IStringList extensionsLowerCase;

  /**
   * Constructor with all invariants.
   *
   * @param aWhatAccepted {@link EFsObjKind} - what is accepted files, directories or both
   * @param aExtensions {@link IStringList} - the extensions (without dot) or an empty list to include all extensions
   * @param aAcceptHidden boolean - if <code>true</code> hidden files and dirs will be accepted
   * @param aCaseSensitive boolean - if <code>true</code> case-sensitive extension check will be performed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFileFilter( EFsObjKind aWhatAccepted, IStringList aExtensions, boolean aAcceptHidden,
      boolean aCaseSensitive ) {
    whatAccepted = TsNullArgumentRtException.checkNull( aWhatAccepted );
    acceptHidden = aAcceptHidden;
    caseSensitive = aCaseSensitive;
    extensions = new StringArrayList( aExtensions );
    IStringListEdit sl = new StringArrayList( aExtensions.size() );
    for( String s : extensions ) {
      sl.add( s.toLowerCase() );
    }
    extensionsLowerCase = sl;
  }

  /**
   * Constricts filter excluding hidden files and case insensitive extensions check.
   *
   * @param aWhatAccepted {@link EFsObjKind} - what is accepted files, direactories or both
   * @param aExtensions String - included extensions (without dot) or an empty list to include all extensions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsFileFilter( EFsObjKind aWhatAccepted, IStringList aExtensions ) {
    this( aWhatAccepted, aExtensions, false, false );
  }

  /**
   * Cretae filter for unhidden files of specified case-insensitive extension.
   *
   * @param aExt String - an extension without dot
   * @return {@link TsFileFilter} - cretaed instance
   */
  public static TsFileFilter ofFileExt( String aExt ) {
    return new TsFileFilter( FILE, new SingleStringList( aExt ), false, false );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines what is accepted files, direactories or both.
   *
   * @return {@link EFsObjKind} - accepted file object types
   */
  public EFsObjKind getWhatAccepted() {
    return whatAccepted;
  }

  /**
   * Determines if hidden files/dirs are accepted.
   *
   * @return boolean if <code>true</code> hidden files and dirs will be accepted
   */
  public boolean isHiddenAccepted() {
    return acceptHidden;
  }

  /**
   * Determines if file extensions are checked case sensitive.
   *
   * @return boolean - <code>true</code> case-sensitive extension check will be performed
   */
  public boolean isExtensionCaseIgnored() {
    return caseSensitive;
  }

  /**
   * returns accepted file extensions.
   *
   * @return {@link IStringList} - the extensions (without dot) or an empty list to include all extensions
   */
  public IStringList getExtensions() {
    return extensions;
  }

  // ------------------------------------------------------------------------------------
  // java.io.FileFilter
  //

  @Override
  public boolean accept( File aPathName ) {
    boolean isFile = false;
    // consider filesystem object
    if( aPathName.isDirectory() ) {
      if( !whatAccepted.isDir() ) {
        return false;
      }
    }
    else {
      if( aPathName.isFile() ) {
        isFile = true;
        if( !whatAccepted.isFile() ) {
          return false;
        }
      }
      else {
        // only files and directories are accepted, other objects (like pipes) are NEVER accepted
        return false;
      }
    }
    // consider the hidden filesystem object
    if( aPathName.isHidden() && !acceptHidden ) {
      return false;
    }
    // consider file extensions
    if( isFile && !extensions.isEmpty() ) {
      String ext = TsFileUtils.extractExtension( aPathName.getName() );
      if( caseSensitive ) {
        return extensions.hasElem( ext );
      }
      return extensionsLowerCase.hasElem( ext.toLowerCase() );
    }
    return true;
  }
}
