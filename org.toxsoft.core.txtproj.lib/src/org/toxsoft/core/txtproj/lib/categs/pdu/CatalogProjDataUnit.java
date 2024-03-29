package org.toxsoft.core.txtproj.lib.categs.pdu;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.txtproj.lib.categs.*;
import org.toxsoft.core.txtproj.lib.categs.impl.*;
import org.toxsoft.core.txtproj.lib.impl.*;

/**
 * Реализация {@link ICatalogueProjDataUnit}.
 *
 * @author hazard157
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class CatalogProjDataUnit
    extends AbstractProjDataUnit
    implements ICatalogueProjDataUnit {

  private final Catalogue catalogue;

  boolean isReading = false;

  /**
   * Конструктор.
   */
  public CatalogProjDataUnit() {
    catalogue = new Catalogue();
    catalogue.eventer().addListener( (ICatalogueChangeListener)( aCatalogue, aOp, aCategoryId ) -> {
      if( isReading ) {
        return;
      }
      genericChangeEventer().fireChangeEvent();
    } );
  }

  // ------------------------------------------------------------------------------------
  // Методы базового класса
  //

  @Override
  protected void doWrite( IStrioWriter aDw ) {
    catalogue.write( aDw );
  }

  @Override
  protected void doRead( IStrioReader aDr ) {
    isReading = true;
    try {
      catalogue.read( aDr );
    }
    finally {
      isReading = false;
    }
  }

  @Override
  protected void doClear() {
    catalogue.clear();
  }

  // ------------------------------------------------------------------------------------
  // ICatalogProjDataUnit
  //

  @Override
  public ICatalogue catalogue() {
    return catalogue;
  }

}
