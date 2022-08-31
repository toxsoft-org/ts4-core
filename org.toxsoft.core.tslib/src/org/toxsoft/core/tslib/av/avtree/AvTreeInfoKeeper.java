package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Механизм сохранения/загрузки дерева значений {@link IAvTreeEdit} и {@link IAvTree} в текстовое представление.
 * <p>
 * Этот класс является реализацией паттерна {@link AbstractEntityKeeper}.
 * <p>
 * Класс является синглтоном, с единственным экземпляром {@link #KEEPER}.
 * <p>
 * <p>
 * Описание дерева значений сохраняется в формате:<br>
 * <code><b>{ id, "description", "name", isArray, {FieldInfo1,...,FieldInfoN}, {NodeInfo1,...NodeInfoN}, isFamily, {MemberInfo1,...MemberInfoN} }</b></code>
 * <br>
 * где:<br>
 * FieldInfo1...FieldInfoN - описания полей в формате <b>{id,descr,name,fieldType,extraOps}<b>, fieldType - кипером
 * {@link DataType#KEEPER}, extrsOps - кипером {@link OptionSetKeeper#KEEPER};<br>
 * NodeInfo1...NodeInfoN - описания узлов, записанный этим же методом {@link #write(IStrioWriter, IAvTreeInfo)}; <br>
 * MemberInfo1...MemberInfoN - описания членов семейства, записанный этим же методом
 * {@link #write(IStrioWriter, IAvTreeInfo)}.
 * <p>
 * Обратите внимание, что метод {@link #read(IStrioReader)} на самом деле возвращает {@link IAvTreeInfoEdit} - то есть,
 * возвращаемое значение можно сразу приводить к {@link IAvTreeInfoEdit}.
 *
 * @author goga
 */
public class AvTreeInfoKeeper
    extends AbstractEntityKeeper<IAvTreeInfo> {

  /**
   * Синглтон класса.
   */
  public static final AvTreeInfoKeeper KEEPER = new AvTreeInfoKeeper();

  private AvTreeInfoKeeper() {
    super( IAvTreeInfo.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IAvTreeInfo aInfo ) {
    aSw.writeAsIs( aInfo.id() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aInfo.description() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aInfo.nmName() );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( aInfo.isArray() );
    aSw.writeSeparatorChar();
    // описания полей
    aSw.writeChar( CHAR_SET_BEGIN );
    for( int i = 0, n = aInfo.fieldInfoes().size(); i < n; i++ ) {
      IAvTreeFieldInfo fInfo = aInfo.fieldInfoes().get( i );
      AvTreeFieldInfo.save( aSw, fInfo );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_SET_END );
    // описания узов
    aSw.writeSeparatorChar();
    aSw.writeChar( CHAR_SET_BEGIN );
    for( int i = 0, n = aInfo.nodeInfoes().size(); i < n; i++ ) {
      IAvTreeInfo info = aInfo.nodeInfoes().get( i );
      doWrite( aSw, info );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_SET_END );
    aSw.writeSeparatorChar();
    aSw.writeBoolean( aInfo.isFamily() );
    aSw.writeSeparatorChar();
    // описания членов семейства
    aSw.writeChar( CHAR_SET_BEGIN );
    for( int i = 0, n = aInfo.familyMembers().size(); i < n; i++ ) {
      IAvTreeInfo info = aInfo.familyMembers().get( i );
      doWrite( aSw, info );
      if( i < n - 1 ) {
        aSw.writeSeparatorChar();
      }
    }
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected IAvTreeInfo doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    boolean isArray = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    // описания полей
    IStridablesListEdit<IAvTreeFieldInfo> fieldInfoes = IStridablesList.EMPTY;
    if( aSr.readSetBegin() ) {
      fieldInfoes = new StridablesList<>();
      do {
        fieldInfoes.add( AvTreeFieldInfo.load( aSr ) );
      } while( aSr.readSetNext() );
    }
    // описания узлов
    aSr.ensureSeparatorChar();
    IStridablesListEdit<IAvTreeInfo> nodeInfoes = IStridablesList.EMPTY;
    if( aSr.readSetBegin() ) {
      nodeInfoes = new StridablesList<>();
      do {
        nodeInfoes.add( doRead( aSr ) );
      } while( aSr.readSetNext() );
    }
    aSr.ensureSeparatorChar();
    boolean isFamily = aSr.readBoolean();
    aSr.ensureSeparatorChar();
    // члены семейств
    IStridablesListEdit<IAvTreeInfo> familyMembers = IStridablesList.EMPTY;
    if( aSr.readSetBegin() ) {
      familyMembers = new StridablesList<>();
      do {
        familyMembers.add( doRead( aSr ) );
      } while( aSr.readSetNext() );
    }
    if( isFamily ) {
      return new AvTreeInfo( id, description, name, familyMembers, isArray );
    }
    return new AvTreeInfo( id, description, name, fieldInfoes, nodeInfoes, isArray );
  }

}
