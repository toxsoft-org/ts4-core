package org.toxsoft.core.txtproj.lib.categs.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.categs.*;

/**
 * Редактируемая реализация {@link ICategory}.
 *
 * @author hazard157
 * @param <T> - конкретный наследник (реализация) категори (но не каталога)
 */
public class Category<T extends ICategory<T>>
    implements ICategory<T>, IParameterizedEdit {

  private final String         id;
  private final IOptionSetEdit params = new OptionSet();
  private final Catalogue<T>   catalogue;
  private final String         parentId;
  private final String         localId;

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  protected Category( Catalogue<?> aCatalogue, String aId, IOptionSet aParams ) {
    catalogue = (Catalogue)TsNullArgumentRtException.checkNull( aCatalogue );
    id = StridUtils.checkValidIdPath( aId );
    params.addAll( aParams );
    parentId = CatalogueUtils.extractParentId( aId );
    localId = StridUtils.getLast( aId );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return DDEF_NAME.getValue( params ).asString();
  }

  @Override
  public String description() {
    return DDEF_DESCRIPTION.getValue( params ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params.getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // ICategory
  //

  @Override
  public ICatalogue<T> catalogue() {
    return catalogue;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  String parentId() {
    return parentId;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return id;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof @SuppressWarnings( "rawtypes" ) ICategory that ) {
      return id.equals( that.id() ) && params.equals( that.params() );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + id.hashCode();
    result = TsLibUtils.PRIME * result + params.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( ICategory<?> aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    return id().compareTo( aThat.id() );
  }

  @Override
  public String localId() {
    return localId;
  }

  @Override
  public ICategory<T> parent() {
    return catalogue.getParent( parentId );
  }

  @Override
  public IStridablesList<T> childCategories() {
    return catalogue.childCategories( id );
  }

  @Override
  public IStridablesList<T> scionCategories() {
    return catalogue.scionCategories( id );
  }

}
