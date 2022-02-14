package org.toxsoft.core.txtproj.lib.categs.pdu;

import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.txtproj.lib.categs.ICatalogue;
import org.toxsoft.core.txtproj.lib.categs.ICatalogueChangeListener;
import org.toxsoft.core.txtproj.lib.categs.impl.Catalogue;
import org.toxsoft.core.txtproj.lib.impl.AbstractProjDataUnit;

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
    catalogue.eventer().addListener( new ICatalogueChangeListener() {

      @Override
      public void onCatalogueChanged( ICatalogue aCatalogue, ECrudOp aOp, String aCategoryId ) {
        if( isReading ) {
          return;
        }
        genericChangeEventer.fireChangeEvent();
      }
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
  // Реализация интерфейса ICatalogProjDataUnit
  //

  @Override
  public ICatalogue catalogue() {
    return catalogue;
  }

}
