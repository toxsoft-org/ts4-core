package org.toxsoft.core.tslib.bricks.pdm;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base abstract implementation of {@link IPdmContent}.
 * <p>
 * Note: {@link #read(IStrioReader)} and {@link #write(IStrioWriter)} method uesureas that first character in stream is
 * an opening brace <code>'<i>{</i>'</code> and last character is a closing brace <code>'<i>}</i>'</code>. So created
 * stream is a section as defined by {@link StrioUtils#readSections(IStrioReader)}.
 *
 * @author hazard157
 */
public non-sealed abstract class PdmAbstractContent
    implements IPdmContent {

  /**
   * Constructor.
   */
  public PdmAbstractContent() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  final public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    aSw.writeChar( CHAR_SET_BEGIN );
    doWrite( aSw );
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  final public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    aSr.ensureChar( CHAR_SET_BEGIN );
    doRead( aSr );
    aSr.ensureChar( CHAR_SET_END );
  }

  // ------------------------------------------------------------------------------------
  // IPdmContent
  //

  @Override
  public abstract boolean isClearContent();

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract void doWrite( IStrioWriter aSw );

  protected abstract void doRead( IStrioReader aSr );

}
