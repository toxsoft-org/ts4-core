package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Attribute field definition for {@link IParameterized} entities based on {@link IDataDef} information.
 *
 * @author hazard157
 * @param <T> - modelled {@link IParameterized} entity type
 */
public class M5StdFieldDefDataDef<T extends IParameterized>
    extends M5AttributeFieldDef<T> {

  private final IDataDef dataDef;

  /**
   * Constructor.
   *
   * @param aDataDef {@link IDataDef} - option definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5StdFieldDefDataDef( IDataDef aDataDef ) {
    this( TsNullArgumentRtException.checkNull( aDataDef ).id(), aDataDef );
  }

  /**
   * Constructor with field ID change.
   *
   * @param aId String - the field ID
   * @param aDataDef {@link IDataDef} - option definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public M5StdFieldDefDataDef( String aId, IDataDef aDataDef ) {
    super( aId, TsNullArgumentRtException.checkNull( aDataDef ) );
    dataDef = aDataDef;
    setName( aDataDef.nmName() );
    setDescription( aDataDef.description() );
    setDefaultValue( aDataDef.defaultValue() );

    // TODO do we need this?
    params().addAll( aDataDef.params() );

    setFlags( M5FF_DETAIL );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns underlying data definition.
   *
   * @return {@link IDataDef} - data definition
   */
  public IDataDef dataDef() {
    return dataDef;
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return dataDef.getValue( aEntity.params() );
  }

}
