package org.toxsoft.core.tsgui.panels.opsedit.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.panels.opsedit.group.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISectionDef} editable implementation.
 * <p>
 * Everything except {@link #id()} of this class are editable via {@link #params()}, {@link #defs()} and
 * {@link #values()} editable API. Note that {@link #nmName()} and {@link #description()} may be changed both with
 * {@link #setNameAndDescription(String, String)} and setting options {@link IAvMetaConstants#DDEF_NAME} and
 * {@link IAvMetaConstants#DDEF_DESCRIPTION} in {@link #params()}.
 *
 * @author hazard157
 */
public final class SectionDef
    implements ISectionDef {

  private final StridableParameterized        stripar;
  private final IStridablesListEdit<IDataDef> defs   = new StridablesList<>();
  private final IOptionSetEdit                values = new OptionSet();

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public SectionDef( String aId ) {
    stripar = new StridableParameterized( aId );
  }

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public SectionDef( String aId, IOptionSet aParams ) {
    stripar = new StridableParameterized( aId, aParams );
  }

  /**
   * Constructor.
   *
   * @param <E> - source object class
   * @param aSrc {@link IStridable} {@link IParameterized} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public <E extends IStridable & IParameterized> SectionDef( E aSrc ) {
    stripar = new StridableParameterized( aSrc );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return stripar.id();
  }

  @Override
  public String nmName() {
    return stripar.nmName();
  }

  @Override
  public String description() {
    return stripar.description();
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSetEdit params() {
    return stripar.params();
  }

  // ------------------------------------------------------------------------------------
  // ISectionDef
  //

  @Override
  public IStridablesListEdit<IDataDef> defs() {
    return defs;
  }

  @Override
  public IOptionSetEdit values() {
    return values;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets name {@link #nmName()} and {@link #description()}.
   *
   * @param aName String - the name
   * @param aDescription String - the description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setNameAndDescription( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    params().setStr( TSID_NAME, aName );
    params().setStr( TSID_DESCRIPTION, aDescription );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return StridUtils.printf( StridUtils.FORMAT_ID_NAME, this );
  }

}
