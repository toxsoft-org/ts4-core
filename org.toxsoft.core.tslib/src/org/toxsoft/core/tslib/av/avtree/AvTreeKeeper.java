package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Механизм сохранения/загрузки дерева значений {@link IAvTreeEdit} и {@link IAvTree} в текстовое представление.
 * <p>
 * Этот класс является реализацией паттерна {@link AbstractEntityKeeper}.
 * <p>
 * Класс является синглтоном, с единственным экземпляром {@link #KEEPER}.
 * <p>
 * Дерево сохраняется в формате:<br>
 * <code><b>{ structId, OptionSet, { Node1, Node2, ... NodeN } }</b></code><br>
 * для единичного дерева или<br>
 * <code><b>{ [ Node1, Node2, ... NodeN ] }</b></code><br>
 * для дерева массива<br>
 * где:<br>
 * OptionSet - значения полей, записанное кипером {@link OptionSetKeeper#KEEPER},<br>
 * Node1 ... NodeN - значения узлов, записанные этим же методом {@link #write(IStrioWriter, IAvTree)},<br>
 * Elem1 ... ElemN - элементы массива, записанные этим же методом {@link #write(IStrioWriter, IAvTree)}.
 * <p>
 * Обратите внимание, что метод {@link #read(IStrioReader)} на самом деле возвращает {@link IAvTreeEdit} - то есть,
 * возвращаемое значение можно сразу приводить к {@link IAvTreeEdit}.
 *
 * @author goga
 */
public class AvTreeKeeper
    extends AbstractEntityKeeper<IAvTree> {

  /**
   * Синглтон класса.
   */
  public static final AvTreeKeeper KEEPER = new AvTreeKeeper();

  private AvTreeKeeper() {
    super( IAvTree.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private final void doSaveSingleAvTreeContent( IStrioWriter aSw, IAvTree aAvTree ) {
    aSw.writeQuotedString( aAvTree.structId() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aAvTree.fields() );
    aSw.writeSeparatorChar();
    // запишем узлы
    aSw.writeChar( CHAR_SET_BEGIN );
    for( int i = 0, n = aAvTree.nodes().size(); i < n; i++ ) {
      String nodeId = aAvTree.nodes().keys().get( i );
      IAvTree nodeTree = aAvTree.nodes().values().get( i );
      aSw.writeAsIs( nodeId );
      aSw.writeChar( CHAR_EQUAL );
      doWrite( aSw, nodeTree );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_SET_END );
  }

  private final void doSaveArrayAvTreeContent( IStrioWriter aSw, IAvTree aAvTree ) {
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    for( int i = 0, n = aAvTree.arrayLength(); i < n; i++ ) {
      IAvTree elem = aAvTree.arrayElement( i );
      doWrite( aSw, elem );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_ARRAY_END );
  }

  private IAvTreeEdit doLoadSingleAvTreeContent( IStrioReader aSr ) {
    String structId = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IOptionSet fields = OptionSetKeeper.KEEPER.read( aSr );
    aSr.ensureChar( CHAR_ITEM_SEPARATOR );
    // считаем узлы
    IStringMapEdit<IAvTree> nodes = IStringMap.EMPTY;
    if( aSr.readSetBegin() ) {
      nodes = new StringMap<>();
      do {
        String nodeId = aSr.readIdPath();
        aSr.ensureChar( CHAR_EQUAL );
        IAvTree node = doRead( aSr );
        nodes.put( nodeId, node );
      } while( aSr.readSetNext() );
    }
    return AvTree.createSingleAvTree( structId, fields, nodes );
  }

  private IAvTreeEdit doLoadArrayAvTreeContent( IStrioReader aSr ) {
    IAvTreeEdit t = AvTree.createArrayAvTree();
    if( aSr.readArrayBegin() ) {
      do {
        IAvTree elem = doRead( aSr );
        t.addElement( elem );
      } while( aSr.readArrayNext() );
    }
    return t;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IAvTree aAvTree ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.writeBoolean( aAvTree.isArray() );
    aSw.writeSeparatorChar();
    if( aAvTree.isArray() ) {
      doSaveArrayAvTreeContent( aSw, aAvTree );
    }
    else {
      doSaveSingleAvTreeContent( aSw, aAvTree );
    }
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected IAvTree doRead( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    boolean isArray = aSr.readBoolean();
    aSr.ensureChar( CHAR_ITEM_SEPARATOR );
    IAvTreeEdit result;
    if( isArray ) {
      result = doLoadArrayAvTreeContent( aSr );
    }
    else {
      result = doLoadSingleAvTreeContent( aSr );
    }
    aSr.ensureChar( CHAR_SET_END );
    return result;
  }

}
