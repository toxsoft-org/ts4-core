package org.toxsoft.core.txtproj.lib.sfv;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.txtproj.lib.sfv.ITsResources.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filebound.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISfvContent} implementation.
 *
 * @author hazard157
 */
public class SfvContent
    implements ISfvContent, IKeepedContent {

  private static final int CONTENT_WARN_SIZE = 100 * 1024 * 1024; // 100M

  private final ITsListValidator<ISfvSection> sectionsChangeValidator = new ITsListValidator<>() {

    private ValidationResult checkSfvSection( ISfvSection aSection ) {
      if( aSection.sectionId().isBlank() ) {
        return ValidationResult.error( FMT_ERR_SECT_ID_BLANK, aSection.sectionId() );
      }
      if( !StridUtils.isValidIdPath( aSection.sectionId() ) ) {
        return ValidationResult.error( FMT_ERR_SECT_ID_NON_IDPATH, aSection.sectionId() );
      }
      if( aSection.sectionContent().length() > CONTENT_WARN_SIZE ) {
        return ValidationResult.warn( FMT_WARN_BIG_CONTENT, aSection.sectionId() );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canReplace( INotifierList<ISfvSection> aSource, ISfvSection aExistingItem,
        ISfvSection aNewItem ) {
      ValidationResult vr = checkSfvSection( aNewItem );
      if( !vr.isError() ) {
        IListEdit<ISfvSection> foundSects = listSectionsById( aNewItem.sectionId() );
        foundSects.remove( aExistingItem );
        if( !foundSects.isEmpty() ) {
          vr = ValidationResult.firstNonOk( vr,
              ValidationResult.warn( FMT_WARN_DUP_SECT_ID_EXIST, aNewItem.sectionId() ) );
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canRemove( INotifierList<ISfvSection> aSource, ISfvSection aRemovingItem ) {
      if( !sectionsList.hasElem( aRemovingItem ) ) {
        return ValidationResult.error( FMT_ERR_SECT_NOT_EXIST, aRemovingItem.sectionId() );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canAdd( INotifierList<ISfvSection> aSource, ISfvSection aNewItem ) {
      ValidationResult vr = checkSfvSection( aNewItem );
      if( !vr.isError() ) {
        if( !listSectionsById( aNewItem.sectionId() ).isEmpty() ) {
          vr = ValidationResult.firstNonOk( vr, ValidationResult.warn( FMT_WARN_SECT_ID_EXIST, aNewItem.sectionId() ) );
        }
      }
      return vr;
    }
  };

  private final GenericChangeEventer eventer;

  private final INotifierListEdit<ISfvSection> sectionsList =
      new NotifierListEditWrapper<>( new ElemLinkedBundleList<>() );

  /**
   * Constructor
   */
  public SfvContent() {
    eventer = new GenericChangeEventer( this );
    sectionsList.addCollectionChangeValidator( sectionsChangeValidator );
    sectionsList.addCollectionChangeListener( eventer );
  }

  // ------------------------------------------------------------------------------------
  // IKeepedContent
  //

  @Override
  public void write( IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNull( aSw );
    for( ISfvSection ss : sectionsList ) {
      aSw.writeAsIs( ss.sectionId() );
      aSw.writeSpace();
      aSw.writeChar( CHAR_EQUAL );
      aSw.writeSpace();
      aSw.writeAsIs( ss.sectionContent() );
      aSw.writeEol();
    }
  }

  @Override
  public void read( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IListEdit<ISfvSection> sects = new ElemLinkedBundleList<>();
    while( aSr.peekChar( EStrioSkipMode.SKIP_COMMENTS ) != CHAR_EOF ) {
      String sectId = aSr.readIdPath();
      aSr.ensureChar( CHAR_EQUAL );
      String content = StrioUtils.readInterbaceContent( aSr );
      ISfvSection ss = new SfvSection( sectId, content );
      sects.add( ss );
    }
    sectionsList.setAll( sects );
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  @Override
  public void clear() {
    sectionsList.clear();
  }

  // ------------------------------------------------------------------------------------
  // ISfvContent
  //

  @Override
  public INotifierListEdit<ISfvSection> sections() {
    return sectionsList;
  }

  @Override
  public IListEdit<ISfvSection> listSectionsById( String aSectionId ) {
    TsNullArgumentRtException.checkNull( aSectionId );
    IListEdit<ISfvSection> ll = new ElemArrayList<>();
    for( ISfvSection ss : sectionsList ) {
      if( ss.sectionId().equals( aSectionId ) ) {
        ll.add( ss );
      }
    }
    return ll;
  }

}
