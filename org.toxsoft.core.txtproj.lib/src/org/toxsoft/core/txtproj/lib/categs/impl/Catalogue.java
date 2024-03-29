package org.toxsoft.core.txtproj.lib.categs.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;
import static org.toxsoft.core.txtproj.lib.categs.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.categs.*;

/**
 * {@link ICatalogue} implementation.
 *
 * @author hazard157
 * @param <T> - concrete type of the category items (not the catalogue)
 */
public class Catalogue<T extends ICategory<T>>
    implements ICatalogue<T>, IKeepableEntity {

  /**
   * {@link ICatalogue#eventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<ICatalogueChangeListener> {

    private boolean isPending = false;

    @Override
    protected void doClearPendingEvents() {
      isPending = false;
    }

    @Override
    protected void doFirePendingEvents() {
      isPending = false;
      fireEvent( ECrudOp.LIST, null );
    }

    @Override
    protected boolean doIsPendingEvents() {
      return isPending;
    }

    void fireEvent( ECrudOp aOp, String aCategoryId ) {
      if( isFiringPaused() ) {
        isPending = true;
        return;
      }
      for( ICatalogueChangeListener l : listeners() ) {
        try {
          l.onCatalogueChanged( Catalogue.this, aOp, aCategoryId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

  }

  /**
   * {@link ICatalogue#svs()} implementation.
   *
   * @author hazard157
   */
  class ValidationSupport
      extends AbstractTsValidationSupport<ICatalogueEditValidator>
      implements ICatalogueEditValidator {

    @Override
    public ICatalogueEditValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreateCategory( String aParentId, String aLocalId, IOptionSet aParams ) {
      TsNullArgumentRtException.checkNulls( aParentId, aLocalId, aParams );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ICatalogueEditValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreateCategory( aParentId, aLocalId, aParams ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canEditCategory( String aId, IOptionSet aParams ) {
      TsNullArgumentRtException.checkNulls( aId, aParams );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ICatalogueEditValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canEditCategory( aId, aParams ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canChangeCaregoryLocalId( String aId, String aNewLocalId ) {
      TsNullArgumentRtException.checkNulls( aId, aNewLocalId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ICatalogueEditValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canChangeCaregoryLocalId( aId, aNewLocalId ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveCategory( String aId ) {
      TsNullArgumentRtException.checkNull( aId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ICatalogueEditValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveCategory( aId ) );
      }
      return vr;
    }

  }

  /**
   * Built-in validator.
   */
  private final ICatalogueEditValidator builtinValidator = new ICatalogueEditValidator() {

    @Override
    public ValidationResult canCreateCategory( String aParentId, String aLocalId, IOptionSet aParams ) {
      // does the parent exists?
      ICategory<T> parent = findCategory( aParentId );
      if( parent == null ) {
        return ValidationResult.error( FMT_ERR_NO_PARENT, aParentId );
      }
      // check local ID
      if( !StridUtils.isValidIdName( aLocalId ) ) {
        return ValidationResult.error( FMT_ERR_INV_LOCAL_ID, aLocalId );
      }
      // does category already exists?
      String id = CatalogueUtils.makeCategoryId( aParentId, aLocalId );
      if( categsList.hasKey( id ) ) {
        return ValidationResult.error( FMT_ERR_CATEG_ALREADY_EXISTS, id );
      }
      return checkParams( id, aParams );
    }

    @Override
    public ValidationResult canEditCategory( String aId, IOptionSet aParams ) {
      // can't edit root catalog
      if( aId.isEmpty() ) {
        return ValidationResult.error( MSG_ERR_CANT_EDIT_ROOT );
      }
      // check edited category existence
      ICategory<T> c = findCategory( aId );
      if( c == null ) {
        return ValidationResult.error( FMT_ERR_NO_CATEG, aId );
      }
      return checkParams( aId, aParams );
    }

    @Override
    public ValidationResult canChangeCaregoryLocalId( String aId, String aNewLocalId ) {
      // can't edit root catalog
      if( aId.isEmpty() ) {
        return ValidationResult.error( MSG_ERR_CANT_EDIT_ROOT );
      }
      // check edited category existence
      ICategory<T> c = findCategory( aId );
      if( c == null ) {
        return ValidationResult.error( FMT_ERR_NO_CATEG, aId );
      }
      // check local ID
      if( !StridUtils.isValidIdName( aNewLocalId ) ) {
        return ValidationResult.error( FMT_ERR_INV_LOCAL_ID, aNewLocalId );
      }
      // check tah category with new ID does not exists
      String newId = CatalogueUtils.makeCategoryId( aId, aNewLocalId );
      c = findCategory( newId );
      if( c != null && !newId.equals( aId ) ) {
        return ValidationResult.error( FMT_ERR_CATEG_ALREADY_EXISTS, newId );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveCategory( String aId ) {
      ICategory<T> c = findCategory( aId );
      if( c == null ) {
        return ValidationResult.error( FMT_ERR_NO_CATEG, aId );
      }
      int scionsCount = c.scionCategories().size();
      if( scionsCount > 0 ) {
        return ValidationResult.warn( FMT_WARN_REMOVING_SUBTREE, aId, Integer.valueOf( scionsCount ) );
      }
      return ValidationResult.SUCCESS;
    }

    private ValidationResult checkParams( String aId, IOptionSet aParams ) {
      if( !aParams.hasKey( TSID_NAME ) ) {
        return ValidationResult.warn( FMT_WARN_NO_NAME, aId );
      }
      if( DDEF_NAME.getValue( aParams ).equals( DDEF_NAME.defaultValue() ) ) {
        return ValidationResult.warn( FMT_WARN_NO_NAME, aId );
      }
      return ValidationResult.SUCCESS;
    }

  };

  private static final ICategoryCreator DEFAULT_CREATOR = Category::new;

  final Eventer           eventer = new Eventer();
  final ValidationSupport svs     = new ValidationSupport();

  final ICategoryCreator            creator;
  final IStridablesListBasicEdit<T> categsList = new SortedStridablesList<>();

  /**
   * Constructor.
   * <p>
   * Creates items directly of type {@link Category} not a subclass
   */
  public Catalogue() {
    svs.addValidator( builtinValidator );
    creator = DEFAULT_CREATOR;
  }

  /**
   * Constructor.
   *
   * @param aCreator {@link ICategoryCreator} - category items creator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public Catalogue( ICategoryCreator aCreator ) {
    svs.addValidator( builtinValidator );
    creator = TsNullArgumentRtException.checkNull( aCreator );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return EMPTY_STRING;
  }

  @Override
  public String nmName() {
    return EMPTY_STRING;
  }

  @Override
  public String description() {
    return EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return IOptionSet.NULL;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    aSw.incNewLine();
    // write categories as "id = { params }"
    for( int i = 0, count = categsList.size(); i < count; i++ ) {
      T c = categsList.get( i );
      aSw.writeAsIs( c.id() );
      aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE );
      OptionSetKeeper.KEEPER.write( aSw, c.params() );
      if( i < count - 1 ) {
        aSw.writeSeparatorChar();
        aSw.writeEol();
      }
    }
    aSw.decNewLine();
    aSw.writeChar( CHAR_ARRAY_END );
  }

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    // read in the temporary collection
    IStringMapEdit<IOptionSet> map = new StringMap<>();
    if( aSr.readArrayBegin() ) {
      do {
        String id = aSr.readIdPath();
        aSr.ensureChar( CHAR_EQUAL );
        IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
        map.put( id, params );
      } while( aSr.readArrayNext() );
    }
    // move read data atomically to the new instance (wicth a validity check)
    IStridablesList<T> saved = new StridablesList<>( categsList );
    eventer.pauseFiring();
    // add new items in a sorted order
    try {
      IStringListBasicEdit sortedIds = new SortedStringLinkedBundleList( map.keys() );
      for( String id : sortedIds ) {
        String parentId = CatalogueUtils.extractParentId( id );
        String localId = CatalogueUtils.extractLocalId( id );
        // do we need to turn off non built-in validators?
        createCategory( parentId, localId, map.getByKey( id ) );
      }
    }
    catch( Exception ex ) {
      categsList.setAll( saved ); // восстановым начальный список
      eventer.resumeFiring( false ); // не было изменений, молча восстановим извещения
      throw new StrioRtException( ex, MSG_ERR_READING_CATALOGUE );
    }
    finally {
      eventer.resumeFiring( false ); // молча восстановим извещения, если есть успех, ниже будет извещение для клиентов
    }
    // reading OK, fire an event
    eventer.fireEvent( ECrudOp.LIST, EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearableCollection
  //

  @Override
  public void clear() {
    if( !categsList.isEmpty() ) {
      categsList.clear();
      eventer.fireEvent( ECrudOp.LIST, EMPTY_STRING );
    }
  }

  // ------------------------------------------------------------------------------------
  // ICategory
  //

  @Override
  public String localId() {
    return EMPTY_STRING;
  }

  @Override
  public ICatalogue<T> catalogue() {
    return this;
  }

  @Override
  public ICategory<T> parent() {
    return null;
  }

  @Override
  public IStridablesList<T> childCategories() {
    IStridablesListEdit<T> ll = new StridablesList<>();
    for( T c : categsList ) {
      if( ((Category<?>)c).parentId().isEmpty() ) {
        ll.add( c );
      }
    }
    return ll;
  }

  @Override
  public IStridablesList<T> scionCategories() {
    return categsList;
  }

  @Override
  public T createCategory( String aParentId, String aLocalId, IOptionSet aParams ) {
    TsValidationFailedRtException.checkError( svs.validator().canCreateCategory( aParentId, aLocalId, aParams ) );
    String id = CatalogueUtils.makeCategoryId( aParentId, aLocalId );
    @SuppressWarnings( "unchecked" )
    T c = (T)creator.create( this, id, aParams );
    categsList.add( c );
    eventer.fireEvent( ECrudOp.CREATE, id );
    return c;
  }

  @Override
  public T editCategory( String aId, IOptionSet aParams ) {
    TsValidationFailedRtException.checkError( svs.validator().canEditCategory( aId, aParams ) );
    T c = categsList.getByKey( aId );
    ((Category<?>)c).params().setAll( aParams );
    eventer.fireEvent( ECrudOp.EDIT, aId );
    return c;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T changeCaregoryLocalId( String aId, String aNewLocalId ) {
    TsValidationFailedRtException.checkError( svs.validator().canChangeCaregoryLocalId( aId, aNewLocalId ) );
    String newChangedId = CatalogueUtils.makeCategoryId( CatalogueUtils.extractParentId( aId ), aNewLocalId );
    if( newChangedId.equals( aId ) ) {
      return getCategory( aId ); // не меняется идентификатор, выходим
    }
    IStridablesList<T> toRemove = listScionCategories( aId, true, true, true );
    IStridablesListEdit<T> toAdd = new StridablesList<>();
    int changedCompIndex = StridUtils.getComponents( aId ).size() - 1;
    for( ICategory<T> cRemove : toRemove ) {
      IStringListEdit comps = new StringArrayList( StridUtils.getComponents( cRemove.id() ) );
      comps.set( changedCompIndex, aNewLocalId );
      String newId = StridUtils.makeIdPath( comps );
      T cNew = (T)creator.create( this, newId, cRemove.params() );
      categsList.removeById( cRemove.id() );
      toAdd.add( cNew );
    }
    categsList.addAll( toAdd );
    eventer.fireEvent( ECrudOp.LIST, newChangedId );
    return getCategory( newChangedId );
  }

  @Override
  public void removeCategory( String aId ) {
    TsValidationFailedRtException.checkError( svs.validator().canRemoveCategory( aId ) );
    IStridablesList<T> toRemoveScions = scionCategories( aId );
    categsList.removeById( aId );
    for( T c : toRemoveScions ) {
      categsList.removeById( c.id() );
    }
    eventer.fireEvent( ECrudOp.LIST, aId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T findCategory( String aCatgoryId ) {
    TsNullArgumentRtException.checkNull( aCatgoryId );
    if( aCatgoryId.isEmpty() ) {
      return (T)this;
    }
    return categsList.findByKey( aCatgoryId );
  }

  @Override
  public T getCategory( String aCatgoryId ) {
    T c = findCategory( aCatgoryId );
    if( c == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_CATEG, aCatgoryId );
    }
    return c;
  }

  @Override
  public IStridablesList<T> listScionCategories( String aCategoryId, boolean aIsSelf, boolean aIsGroups,
      boolean aIsLeafs ) {
    T root = getCategory( aCategoryId );
    IStridablesList<T> scions = scionCategories( aCategoryId );
    IStridablesListEdit<T> ll = new StridablesList<>();
    if( aIsSelf ) {
      ll.add( root );
    }
    for( T t : scions ) {
      boolean leaf = t.isLeaf();
      if( (leaf && aIsLeafs) || (!leaf && aIsGroups) ) {
        ll.add( t );
      }
    }
    return ll;
  }

  @Override
  public ITsEventer<ICatalogueChangeListener> eventer() {
    return eventer;
  }

  @Override
  public ITsValidationSupport<ICatalogueEditValidator> svs() {
    return svs;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  IStridablesList<T> childCategories( String aCategoryId ) {
    TsNullArgumentRtException.checkNull( aCategoryId );
    // отельно отработаем запрос к корню, хотя категории и не должны запрашивать такое...
    if( aCategoryId.isEmpty() ) {
      return childCategories();
    }
    // найдем искомую категорию в отсортированном списке
    int index = categsList.keys().indexOf( aCategoryId );
    TsInternalErrorRtException.checkTrue( index < 0 );
    //
    String prefix = aCategoryId + StridUtils.CHAR_ID_PATH_DELIMITER;
    IStridablesListEdit<T> ll = IStridablesList.EMPTY; // lazy инициализация
    int count = categsList.size();
    // пройдем от (потенциально) первой дочки и до конца списка
    int childCompsNum = StridUtils.getComponents( aCategoryId ).size() + 1; // кол-во компонент в непосредственной дочке
    for( int i = index + 1; i < count; i++ ) {
      T c = categsList.get( i );
      // закончились потомки?
      if( !c.id().startsWith( prefix ) ) {
        break;
      }
      if( StridUtils.getComponents( c.id() ).size() == childCompsNum ) {
        // lazy инициализация списка ll
        if( ll == IStridablesList.EMPTY ) {
          ll = new StridablesList<>();
        }
        ll.add( c );
      }
    }
    return ll;
  }

  IStridablesList<T> scionCategories( String aCategoryId ) {
    TsNullArgumentRtException.checkNull( aCategoryId );
    // отельно отработаем запрос к корню, хотя категории и не должны запрашивать такое...
    if( aCategoryId.isEmpty() ) {
      return scionCategories();
    }
    // найдем искомую категорию в отсортированном списке
    int index = categsList.keys().indexOf( aCategoryId );
    TsInternalErrorRtException.checkTrue( index < 0 );
    //
    String prefix = aCategoryId + StridUtils.CHAR_ID_PATH_DELIMITER;
    IStridablesListEdit<T> ll = IStridablesList.EMPTY; // lazy инициализация
    int count = categsList.size();
    // пройдем от (потенциально) первой дочки и до конца списка
    for( int i = index + 1; i < count; i++ ) {
      T c = categsList.get( i );
      // закончились потомки?
      if( !c.id().startsWith( prefix ) ) {
        break;
      }
      // lazy инициализация списка ll
      if( ll == IStridablesList.EMPTY ) {
        ll = new StridablesList<>();
      }
      ll.add( c );
    }
    return ll;
  }

  ICategory<T> getParent( String aParentId ) {
    if( aParentId.isEmpty() ) {
      return this;
    }
    return categsList.getByKey( aParentId );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return "(catalogue)"; //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aThat ) {
    return aThat == this;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
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

}
