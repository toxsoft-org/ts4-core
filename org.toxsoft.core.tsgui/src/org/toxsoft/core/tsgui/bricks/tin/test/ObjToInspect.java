package org.toxsoft.core.tsgui.bricks.tin.test;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Temporary class - an object to be inspected.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public final class ObjToInspect {

  private final String       name;
  private final ITinTypeInfo entityInfo;

  private Object entity;

  public ObjToInspect( String aName, ITinTypeInfo aEntityInfo, Object aEntity ) {
    TsNullArgumentRtException.checkNulls( aName, aEntityInfo, aEntity );
    name = aName;
    entityInfo = aEntityInfo;
    entity = aEntity;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public String name() {
    return String.format( "%s = '%s'", name, entity ); //$NON-NLS-1$
  }

  public ITinTypeInfo entityInfo() {
    return entityInfo;
  }

  public Object entity() {
    return entity;
  }

  public void setEntity( Object aEntity ) {
    TsNullArgumentRtException.checkNull( aEntity );
    entity = aEntity;
  }

}
