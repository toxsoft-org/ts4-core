package org.toxsoft.core.tslib.av.avtree;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Неизменяемая реализация {@link IAvTreeFieldInfo}.
 *
 * @author goga
 */
public class AvTreeFieldInfo
    extends Stridable
    implements IAvTreeFieldInfo {

  private final IDataType  fieldType;
  private final IOptionSet extraOps;

  /**
   * Создает описание со всеми инвариантами.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aDescription String - удобочитаемое описание поля
   * @param aName String - краткое, удобочитаемое имя поля
   * @param aFieldType {@link IDataType} - тип данных поля
   * @param aExtraOps {@link IOptionSet} - произвольные дополнительные параметры
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-путь
   */
  public AvTreeFieldInfo( String aId, String aDescription, String aName, IDataType aFieldType, IOptionSet aExtraOps ) {
    super( aId, aDescription, aName, true );
    fieldType = TsNullArgumentRtException.checkNull( aFieldType );
    extraOps = new OptionSet( aExtraOps );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSource {@link IAvTreeFieldInfo} - исходный объект
   * @throws TsNullArgumentRtException аргумент = null
   */
  public AvTreeFieldInfo( IAvTreeFieldInfo aSource ) {
    this( TsNullArgumentRtException.checkNull( aSource ).id(), aSource.description(), aSource.nmName(),
        aSource.fieldType(), aSource.extraOps() );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  static void save( IStrioWriter aSw, IAvTreeFieldInfo aInfo ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.writeAsIs( aInfo.id() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aInfo.description() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aInfo.nmName() );
    aSw.writeSeparatorChar();
    DataType.KEEPER.write( aSw, aInfo.fieldType() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER.write( aSw, aInfo.extraOps() );
    aSw.writeChar( CHAR_SET_END );
  }

  static IAvTreeFieldInfo load( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IDataType fieldType = DataType.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    IOptionSet extraOps = OptionSetKeeper.KEEPER.read( aSr );
    aSr.ensureChar( CHAR_SET_END );
    return new AvTreeFieldInfo( id, description, name, fieldType, extraOps );
  }

  // ------------------------------------------------------------------------------------
  // IAvTreeFieldInfo
  //

  @Override
  public IDataType fieldType() {
    return fieldType;
  }

  @Override
  public IOptionSet extraOps() {
    return extraOps;
  }

}
