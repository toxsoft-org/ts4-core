package org.toxsoft.tslib.bricks.keeper.std;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.strio.*;
import org.toxsoft.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringLinkedBundleList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link String} keeper.
 *
 * @author hazard157
 */
public class StringKeeper
    extends AbstractEntityKeeper<String> {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "String"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static StringKeeper KEEPER = new StringKeeper();

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
   * Readsa items in {@link IStringListEdit}.
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
