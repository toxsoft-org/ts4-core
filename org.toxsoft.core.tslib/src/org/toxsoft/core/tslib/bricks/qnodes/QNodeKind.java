package org.toxsoft.core.tslib.bricks.qnodes;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQNodeKind} implementation.
 *
 * @author hazard157
 * @param <T> - node entity type
 */
public final class QNodeKind<T>
    extends StridableParameterized
    implements IQNodeKind<T> {

  private final Class<T> entityClass;
  private final boolean  canHaveChilds;
  private final String   iconId;

  /**
   * Constructor.
   *
   * @param aId String - note kind ID
   * @param aName String - node kind short name
   * @param aDescription String - node kind description
   * @param aClass {@link Class} - type of the node entity {@link IQNode#entity()}
   * @param aCanHaveChilds boolean - flags that node can have childs
   * @param aIconId String - icon ID, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public QNodeKind( String aId, String aName, String aDescription, Class<T> aClass, boolean aCanHaveChilds,
      String aIconId ) {
    super( aId );
    TsNullArgumentRtException.checkNull( aClass );
    entityClass = aClass;
    canHaveChilds = aCanHaveChilds;
    iconId = aIconId;
  }

  /**
   * Constructor with empty name and description.
   *
   * @param aId String - note kind ID
   * @param aClass {@link Class} - type of the node entity {@link IQNode#entity()}
   * @param aCanHaveChilds boolean - flags that node can have childs
   * @param aIconId String - icon ID, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public QNodeKind( String aId, Class<T> aClass, boolean aCanHaveChilds, String aIconId ) {
    this( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, aClass, aCanHaveChilds, aIconId );
  }

  /**
   * Constructor with empty name and description and no icon.
   *
   * @param aId String - note kind ID
   * @param aClass {@link Class} - type of the node entity {@link IQNode#entity()}
   * @param aCanHaveChilds boolean - flags that node can have childs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public QNodeKind( String aId, Class<T> aClass, boolean aCanHaveChilds ) {
    this( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, aClass, aCanHaveChilds, null );
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return iconId;
  }

  // ------------------------------------------------------------------------------------
  // IQNodeKind
  //

  @Override
  public Class<T> entityClass() {
    return entityClass;
  }

  @Override
  public boolean canHaveChilds() {
    return canHaveChilds;
  }

}
