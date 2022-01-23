package org.toxsoft.unit.txtproj.core.categs.pdu;

import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.coll.helpers.ECrudOp;
import org.toxsoft.unit.txtproj.core.categs.ICatalogue;
import org.toxsoft.unit.txtproj.core.categs.ICatalogueChangeListener;
import org.toxsoft.unit.txtproj.core.categs.impl.Catalogue;
import org.toxsoft.unit.txtproj.core.impl.AbstractProjDataUnit;

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
