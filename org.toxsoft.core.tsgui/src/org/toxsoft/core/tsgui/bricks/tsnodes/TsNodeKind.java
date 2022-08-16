package org.toxsoft.core.tsgui.bricks.tsnodes;

import org.toxsoft.core.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link ITsNodeKind} implementation.
 *
 * @author hazard157
 * @param <T> - node entity type
 */
public class TsNodeKind<T>
    extends Stridable
    implements ITsNodeKind<T> {

  private final Class<T> entityClass;
  private final boolean  canHaveChilds;
  private final String   iconId;

  /**
   * Constructor.
   *
   * @param aId String - note kind ID
   * @param aName String - node kind short name
   * @param aDescription String - node kind description
   * @param aClass {@link Class} - type of the node entity {@link ITsNode#entity()}
   * @param aCanHaveChilds boolean - flags that node can have childs
   * @param aIconId String - icon ID, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TsNodeKind( String aId, String aName, String aDescription, Class<T> aClass, boolean aCanHaveChilds,
      String aIconId ) {
    super( aId, aDescription, aName );
    TsNullArgumentRtException.checkNull( aClass );
    entityClass = aClass;
    canHaveChilds = aCanHaveChilds;
    iconId = aIconId;
  }

  /**
   * Constructor with empty name and description.
   *
   * @param aId String - note kind ID
   * @param aClass {@link Class} - type of the node entity {@link ITsNode#entity()}
   * @param aCanHaveChilds boolean - flags that node can have childs
   * @param aIconId String - icon ID, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TsNodeKind( String aId, Class<T> aClass, boolean aCanHaveChilds, String aIconId ) {
    this( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, aClass, aCanHaveChilds, aIconId );
  }

  /**
   * Constructor with empty name and description and no icon.
   *
   * @param aId String - note kind ID
   * @param aClass {@link Class} - type of the node entity {@link ITsNode#entity()}
   * @param aCanHaveChilds boolean - flags that node can have childs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public TsNodeKind( String aId, Class<T> aClass, boolean aCanHaveChilds ) {
    this( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, aClass, aCanHaveChilds, null );
  }

  @Override
  public Class<T> entityClass() {
    return entityClass;
  }

  @Override
  public boolean canHaveChilds() {
    return canHaveChilds;
  }

  @Override
  public String iconId() {
    return iconId;
  }

  @Override
  public String getEntityName( T aEntity ) {
    return doGetEntityName( aEntity );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may implement its own strategy do determine node displayed name.
   * <p>
   * In the base class, it simply returns <code>null</code>, when overridden, there's no need to call the superclass
   * method.
   *
   * @param aEntity &lt;T&gt; - entity in the node {@link ITsNode#entity()}, may be <code>null</code>
   * @return String - displayed node name or <code>null</code>
   */
  protected String doGetEntityName( T aEntity ) {
    return null;
  }

}
