package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Механизм сохранения/загрузки дерева значений {@link IAvTreeEdit} и {@link IAvTree} в текстовое представление.
 * <p>
 * Этот класс является реализацией паттерна {@link AbstractEntityKeeper}.
 * <p>
 * Класс является синглтоном, с единственным экземпляром {@link #KEEPER}.
 * <p>
 * Путь записывается как <code>{ {true,id1,isArray,arrIndex}, ... {false,idN,isArray,arrIndex} }</code>.
 *
 * @author hazard157
 */
public class AvTreePathKeeper
    extends AbstractEntityKeeper<AvTreePath> {

  /**
   * Синглтон класса.
   */
  public static final AvTreePathKeeper KEEPER = new AvTreePathKeeper();

  private AvTreePathKeeper() {
    super( AvTreePath.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //
  @Override
  protected void doWrite( IStrioWriter aSw, AvTreePath aPath ) {
    AvTreePathElementKeeper.KEEPER.writeColl( aSw, aPath.elements(), false );
  }

  @Override
  protected AvTreePath doRead( IStrioReader aDr ) {
    IList<IAvTreePathElement> elems = AvTreePathElementKeeper.KEEPER.readColl( aDr );
    return new AvTreePath( elems );
  }

}
