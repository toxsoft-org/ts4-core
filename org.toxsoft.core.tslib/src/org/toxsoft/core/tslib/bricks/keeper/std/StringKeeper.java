package org.toxsoft.core.tslib.bricks.keeper.std;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link String} keeper.
 *
 * @author hazard157
 */
public class StringKeeper
    extends AbstractEntityKeeper<String> {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "String"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final StringKeeper KEEPER = new StringKeeper();

  private StringKeeper() {
    super( String.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, String aEntity ) {
    aSw.writeQuotedString( aEntity );
  }

  @Override
  protected String doRead( IStrioReader aSr ) {
    return aSr.readQuotedString();
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  /**
   * Reads items in {@link IStringListEdit}.
   *
   * @param aSr {@link IStrioReader} - читатель текстового представления
   * @return {@link IStringListEdit} - редактируемый список считанных строк
   * @throws TsNullArgumentRtException аргумент = null
   * @throws StrioRtException синтаксическая ошибка чтения
   */
  public IStringListEdit readStringList( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    IStringListEdit list = new StringLinkedBundleList();
    if( aSr.readArrayBegin() ) {
      do {
        list.add( doRead( aSr ) );
      } while( aSr.readArrayNext() );
    }
    return list;
  }

}
