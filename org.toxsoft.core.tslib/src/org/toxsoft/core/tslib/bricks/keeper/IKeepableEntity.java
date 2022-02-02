package org.toxsoft.core.tslib.bricks.keeper;

import java.io.IOException;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.TsIoRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Mixin interface of entities whose content can be stored and restored into the text representation.
 * <p>
 * Text representation format is determined by implementation so that {@link #read(IStrioReader)} must fully and
 * correctly restore entity content from written by {@link #write(IStrioWriter)} text.
 * <p>
 * Differences between {@link IKeepableEntity} and {@link IEntityKeeper} is described in {@link IEntityKeeper}.
 *
 * @see IEntityKeeper
 * @author hazard157
 */
public interface IKeepableEntity {

  /**
   * Stores entity content into text representation.
   *
   * @param aSw {@link IStrioWriter} - text representation writer
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured during write
   */
  void write( IStrioWriter aSw );

  /**
   * Restores entity content into text representation.
   *
   * @param aSr {@link IStrioReader} - text representation reader
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured during read
   * @throws StrioRtException invalid text representation format
   */
  void read( IStrioReader aSr );

}
