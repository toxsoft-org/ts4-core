package org.toxsoft.core.tslib.bricks.keeper.std;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link Integer} keeper.
 *
 * @author hazard157
 */
public class IntegerKeeper
    extends AbstractEntityKeeper<Integer> {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "Integer"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static IntegerKeeper KEEPER = new IntegerKeeper();

  private IntegerKeeper() {
    super( Integer.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, Integer aEntity ) {
    aSw.writeInt( aEntity.intValue() );
  }

  @Override
  protected Integer doRead( IStrioReader aSr ) {
    return Integer.valueOf( aSr.readInt() );
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  /**
   * Readsa items in {@link IIntListEdit}.
   *
   * @param aSr {@link IStrioReader} - читатель текстового представления
   * @return {@link IIntListEdit} - редактируемый список считанных строк
   * @throws TsNullArgumentRtException аргумент = null
   * @throws StrioRtException синтаксическая ошибка чтения
   */
  public IIntListEdit readIntegerList( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IIntListEdit list = new IntLinkedBundleList();
    if( aSr.readArrayBegin() ) {
      do {
        list.add( doRead( aSr ) );
      } while( aSr.readArrayNext() );
    }
    return list;
  }

}
