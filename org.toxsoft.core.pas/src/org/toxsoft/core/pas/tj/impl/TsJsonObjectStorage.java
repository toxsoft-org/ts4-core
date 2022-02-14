package org.toxsoft.core.pas.tj.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;

/**
 * Загрузка/сохранение сущностей {@link ITjObject} в JSON представление.
 *
 * @author goga
 */
class TsJsonObjectStorage {

  public static TsJsonObjectStorage STORAGE = new TsJsonObjectStorage();

  TsJsonObjectStorage() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public ITjObject load( IStrioReader aReader ) {
    ITjObject obj = new TjObject();
    if( aReader.readArrayBegin() ) {
      do {
        String fieldName = aReader.readQuotedString();
        aReader.ensureChar( ':' );
        ITjValue fieldValue = TsJsonValueStorage.STORAGE.load( aReader );
        obj.fields().put( fieldName, fieldValue );
      } while( aReader.readArrayNext() );
    }
    return obj;
  }

  public void save( IStrioWriter aWriter, ITjObject aObject ) {
    aWriter.writeChar( CHAR_SET_BEGIN );
    // запись пустого объекта
    if( aObject.fields().isEmpty() ) {
      aWriter.writeChar( CHAR_SET_END );
      return;
    }
    aWriter.incNewLine();
    // записи "имя_поля": значение_поля,
    for( int i = 0, count = aObject.fields().size(); i < count; i++ ) {
      String fieldName = aObject.fields().keys().get( i );
      ITjValue fieldValue = aObject.fields().values().get( i );
      aWriter.writeQuotedString( fieldName );
      aWriter.writeChar( ':' );
      aWriter.writeSpace();
      TsJsonValueStorage.STORAGE.save( aWriter, fieldValue );
      if( i < count - 1 ) {
        aWriter.writeSeparatorChar();
        aWriter.writeEol();
      }
    }
    aWriter.decNewLine();
    aWriter.writeChar( CHAR_SET_END );
  }

}
