package org.toxsoft.core.txtproj.lib.impl;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Abstract implementation of {@link IProjDataUnit}.
 *
 * @author hazard157
 */
public abstract class AbstractProjDataUnit
    implements IProjDataUnit {

  private final GenericChangeEventer genericChangeEventer;

  /**
   * Notifier listener to make it easier to implement subclasses.
   */
  protected final ITsCollectionChangeListener collectionChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      genericChangeEventer.fireChangeEvent();
    }
  };

  /**
   * Constructor.
   */
  protected AbstractProjDataUnit() {
    genericChangeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  final public void write( IStrioWriter aDw ) {
    TsNullArgumentRtException.checkNull( aDw );
    doWrite( aDw );
  }

  @Override
  final public void read( IStrioReader aDr ) {
    TsNullArgumentRtException.checkNull( aDr );
    try {
      doRead( aDr );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  final public void clear() {
    try {
      doClear();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  // ------------------------------------------------------------------------------------
  // IProjDataUnit
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * The inheritor must write the content to the output stream.
   * <p>
   * <b>Note:</b> The first character (excluding spaces) must be one of the opening brackets
   * {@link IStrioHardConstants#CHAR_ARRAY_BEGIN} or {@link IStrioHardConstants#CHAR_SET_BEGIN}. The last character (not
   * counting spaces) must be the pair of the opening closing brace. *
   *
   * @param aSw {@link IStrioWriter} - output stream, never is <code>null</code>
   * @throws TsIoRtException stream I/O error
   */
  abstract protected void doWrite( IStrioWriter aSw );

  /**
   * The subclass must read the content from the stream, previously written by the {@link #doWrite(IStrioWriter)}
   * method.
   * <p>
   * In case of changes method must fire generic change event.
   *
   * @param aSr {@link IStrioReader} - input stream, never is <code>null</code>
   * @throws TsIoRtException stream I/O error
   * @throws StrioRtException invalid data format
   */
  abstract protected void doRead( IStrioReader aSr );

  /**
   * Implementation must clear the content of the unit as it was immediately after constructor.
   * <p>
   * In case of changes method must fire generic change event.
   * <p>
   * This method is called from {@link #clear()}.
   */
  abstract protected void doClear();

}
