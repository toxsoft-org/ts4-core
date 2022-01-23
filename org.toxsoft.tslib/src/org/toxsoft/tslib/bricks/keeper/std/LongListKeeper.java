package org.toxsoft.tslib.bricks.keeper.std;

import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.coll.primtypes.ILongList;
import org.toxsoft.tslib.coll.primtypes.ILongListEdit;
import org.toxsoft.tslib.coll.primtypes.impl.LongLinkedBundleList;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * Хранитель объектов типа {@link ILongList}.
 * <p>
 * Считанное зхначение можно безопасно приводить к {@link ILongListEdit}.
 *
 * @author hazard157
 */
public class LongListKeeper
    extends AbstractEntityKeeper<ILongList> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static IEntityKeeper<ILongList> KEEPER = new LongListKeeper();

  /**
   * Текстовое представление пустого списка.
   */
  public static String EMPTY_LIST = KEEPER.ent2str( ILongList.EMPTY );

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "LongList"; //$NON-NLS-1$

  private LongListKeeper() {
    super( ILongList.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, ILongList aEntity ) {
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    for( int i = 0, n = aEntity.size(); i < n; i++ ) {
      aSw.writeLong( aEntity.getValue( i ) );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
  }

  @Override
  protected ILongList doRead( IStrioReader aSr ) {
    ILongListEdit result = new LongLinkedBundleList();
    if( aSr.readArrayBegin() ) {
      do {
        result.add( aSr.readLong() );
      } while( aSr.readArrayNext() );
    }
    return result;
  }

}
