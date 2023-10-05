package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.devel.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedDecorator} base implementation.
 *
 * @author hazard157
 */
public class VedAbstractDecorator
    extends VedAbstractSnippet
    implements IVedDecorator {

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAbstractDecorator( VedScreen aScreen ) {
    super( aScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractSnippet
  //

  @Override
  final public boolean isActive() {
    if( vedScreen().model().screenDecoratorsBefore().isActive( this ) || //
        vedScreen().model().screenDecoratorsAfter().isActive( this ) ) {
      return true;
    }
    for( String viselId : vedScreen().model().visels().listAllItems().keys() ) {
      IVedSnippetManager<VedAbstractDecorator> dm = vedScreen().model().viselDecoratorsBefore( viselId );
      if( dm.isActive( this ) ) {
        return true;
      }
      dm = vedScreen().model().viselDecoratorsAfter( viselId );
      if( dm.isActive( this ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  final public void setActive( boolean aActive ) {
    vedScreen().model().screenDecoratorsBefore().setActive( this, aActive );
    vedScreen().model().screenDecoratorsAfter().setActive( this, aActive );
    for( String viselId : vedScreen().model().visels().listAllItems().keys() ) {
      vedScreen().model().viselDecoratorsBefore( viselId ).setActive( this, true );
      vedScreen().model().viselDecoratorsAfter( viselId ).setActive( this, true );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsPaintable
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // IPointsHost
  //

  @Override
  public boolean isYours( double aX, double aY ) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ITsRectangle bounds() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IVedDecorator
  //

  @Override
  public String getViselIdOfDrawingTransform() {
    // TODO Auto-generated method stub
    return null;
  }

}
