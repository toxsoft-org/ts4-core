package org.toxsoft.tslib.bricks.filter.impl;

import java.io.Serializable;

import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.tslib.av.opset.impl.OptionSet;
import org.toxsoft.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.tslib.bricks.filter.ITsSingleFilterFactory;
import org.toxsoft.tslib.bricks.filter.ITsSingleFilterParams;
import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ITsSingleFilterParams} implementation.
 *
 * @author hazard157
 */
public class TsSingleFilterParams
    implements ITsSingleFilterParams, Serializable {

  private static final long serialVersionUID = 157157L;

  private final String         typeId;
  private final IOptionSetEdit params = new OptionSet();

  /**
   * Constructor.
   *
   * @param aTypeId String - filter identifier (IDpath) must match factory id {@link ITsSingleFilterFactory#id()}
   * @param aParams {@link IOptionSet} - filter parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public TsSingleFilterParams( String aTypeId, IOptionSet aParams ) {
    typeId = StridUtils.checkValidIdPath( aTypeId );
    params.setAll( aParams );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsSingleFilterParams} - source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsSingleFilterParams( ITsSingleFilterParams aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    typeId = aSource.typeId();
    params.setAll( aSource.params() );
  }

  /**
   * Статический конструктор с пустыми параметрами фильтра.
   *
   * @param aTypeId String - filter identifier (IDpath) must match factory id {@link ITsSingleFilterFactory#id()}
   * @return {@link TsSingleFilterParams} - созданный экземпляр
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public static TsSingleFilterParams create( String aTypeId ) {
    return new TsSingleFilterParams( aTypeId, IOptionSet.NULL );
  }

  /**
   * Статический конструктор с параметрами фильтра.
   *
   * @param aTypeId String - filter identifier (IDpath) must match factory id {@link ITsSingleFilterFactory#id()}
   * @param aIdsAndValues Object[] - параметры (как для {@link OptionSetUtils#createOpSet(Object...)})
   * @return {@link TsSingleFilterParams} - созданный экземпляр
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public static TsSingleFilterParams create( String aTypeId, Object... aIdsAndValues ) {
    return new TsSingleFilterParams( aTypeId, OptionSetUtils.createOpSet( aIdsAndValues ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFpFilterInfo
  //

  @Override
  public String typeId() {
    return typeId;
  }

  @Override
  public IOptionSetEdit params() {
    return params;
  }

}
