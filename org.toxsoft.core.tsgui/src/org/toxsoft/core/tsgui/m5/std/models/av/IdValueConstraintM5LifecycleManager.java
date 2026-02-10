package org.toxsoft.core.tsgui.m5.std.models.av;

import static org.toxsoft.core.tsgui.m5.std.models.av.ITsResources.*;

import java.util.function.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.av.IdValueConstraintM5LifecycleManager.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.metainfo.constr.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * LM for the model {@link IdValueConstraintM5Model}.
 * <p>
 * Note on usage: LM is designed to edit constraints of the data type with fixed atomic type. Each time, when atomic
 * type is changed, LM must be recreated with new atomic type. This behavior is due to the fact that when atomic type
 * changes constraints list must be updated too, but LM can't handle such update.
 *
 * @author hazard157
 */
public class IdValueConstraintM5LifecycleManager
    extends M5LifecycleManager<IdValue, Master> {

  /**
   * Used as a {@link IM5LifecycleManager#master()}, contains atomic type and constraints of the edited
   * {@link IDataType}.
   *
   * @author hazard157
   * @param atomicType - atomic type from {@link IDataType#atomicType()}
   * @param constraints - edited constraints to be used as {@link IDataType#params()}
   */
  public record Master ( Supplier<EAtomicType> atomicType, IOptionSetEdit constraints ) {}

  /**
   * Constructor.
   *
   * @param aModel {@link IdValueConstraintM5Model}&lt;T&gt; - the model, must be {@link IdValueConstraintM5Model}
   * @param aMaster &lt;{@link Master}&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public IdValueConstraintM5LifecycleManager( IdValueConstraintM5Model aModel, Master aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Validates constraint ID and value assuming that ID is an IDpath and checked not duplicates an existing ID.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  private ValidationResult validateConstraint( IM5Bunch<IdValue> aValues ) {
    String cid = model().ID.getFieldValue( aValues ).asString();
    IConstraintInfo cinf = ConstraintUtils.findConstraintInfo( cid );
    // validate only known constraint, for unknown constraint any value is acceptable
    if( cinf != null ) {
      IAtomicValue av = model().VALUE.getFieldValue( aValues );
      return cinf.validateConstraint( master().atomicType().get(), av );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  public IdValueConstraintM5Model model() {
    return (IdValueConstraintM5Model)super.model();
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<IdValue> aValues ) {
    // check ID is an IDpath
    String cid = model().ID.getFieldValue( aValues ).asString();
    ValidationResult vr = StridUtils.validateIdPath( cid );
    if( vr.isError() ) {
      return vr;
    }
    // check there is no constraint with the same ID
    if( master().constraints().hasKey( cid ) ) {
      return ValidationResult.error( FMT_ERR_CONSTRAINT_ID_EXISTS, cid );
    }
    return ValidationResult.firstNonOk( vr, validateConstraint( aValues ) );
  }

  @Override
  protected IdValue doCreate( IM5Bunch<IdValue> aValues ) {
    String id = model().ID.getFieldValue( aValues ).asString();
    IAtomicValue value = model().VALUE.getFieldValue( aValues );
    master().constraints().put( id, value );
    return new IdValue( id, value );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<IdValue> aValues ) {
    // check ID is an IDpath
    String cid = model().ID.getFieldValue( aValues ).asString();
    ValidationResult vr = StridUtils.validateIdPath( cid );
    if( vr.isError() ) {
      return vr;
    }
    // TODO check on ID change there is no constraint with the same ID
    if( !cid.equals( aValues.originalEntity().id() ) ) {
      if( master().constraints().hasKey( cid ) ) {
        return ValidationResult.error( FMT_ERR_CONSTRAINT_ID_EXISTS, cid );
      }
    }
    return validateConstraint( aValues );
  }

  @Override
  protected IdValue doEdit( IM5Bunch<IdValue> aValues ) {
    String id = model().ID.getFieldValue( aValues ).asString();
    IAtomicValue value = model().VALUE.getFieldValue( aValues );
    if( master().constraints().hasKey( id ) ) {
      master().constraints().removeByKey( id );
    }
    master().constraints().put( id, value );
    return new IdValue( id, value );
  }

  @Override
  protected ValidationResult doBeforeRemove( IdValue aEntity ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( IdValue aEntity ) {
    master().constraints().removeByKey( aEntity.id() );
  }

  @Override
  protected IList<IdValue> doListEntities() {
    IListEdit<IdValue> llIdvals = new ElemArrayList<>( master().constraints().size() );
    for( String id : master().constraints().keys() ) {
      llIdvals.add( new IdValue( id, master().constraints().getByKey( id ) ) );
    }
    return llIdvals;
  }

}
